package cn.bossge.cloud_diary_sso.service.impl;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import cn.bossge.cloud_diary_common.service.TokenService;
import cn.bossge.cloud_diary_sso.constant.AccountMessage;
import cn.bossge.cloud_diary_sso.dto.AccountDTO;
import cn.bossge.cloud_diary_sso.entity.Account;
import cn.bossge.cloud_diary_sso.entity.Audit;
import cn.bossge.cloud_diary_sso.enums.AccountAction;
import cn.bossge.cloud_diary_sso.enums.AccountState;
import cn.bossge.cloud_diary_sso.exception.AccountRuntimeException;
import cn.bossge.cloud_diary_sso.repository.AccountRepository;
import cn.bossge.cloud_diary_sso.repository.AuditRepository;
import cn.bossge.cloud_diary_sso.service.AccountService;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
@Log4j2
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AuditRepository auditRepository;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    VelocityEngine engine;

    @Value("${adminemail}")
    String adminEmail;
    
    
    // TODO forbid IP
    @Override
    public void register(AccountDTO account) {
        String email = account.getEmail();
        String password = account.getPassword();
        Account oldAccount = accountRepository.findByEmail(email);
        if (oldAccount == null) {
            Account newAccount = new Account();
            newAccount.createByEmailAndPassword(email, password);
            newAccount = accountRepository.save(newAccount);
            Audit newAudit = Audit.builder().accountId(newAccount.getId()).build();
            newAudit.updateByOperationAndIp(AccountAction.REGISTRATION.name(), account.getIp());
            newAudit.generateVerifyCode();
            auditRepository.save(newAudit);
            sendRegistrationEmail(email, newAudit.getVerifyCode());
        } else {
            if (oldAccount.isPendingForActive()) {
                Audit audit = auditRepository.findTopByAccountIdOrderByOperationDateDesc(oldAccount.getId());
                if (audit.getOperationDate().plusMinutes(5L).isAfter(LocalDateTime.now())) {
                    throw new AccountRuntimeException(AccountMessage.REGISTRATION_PENDING_FOR_ACTIVE_ERROR);
                } else {
                    oldAccount.modifyPassword(password);
                    accountRepository.save(oldAccount);
                    audit.generateVerifyCode();
                    audit.updateByOperationAndIp(AccountAction.REGISTRATION.name(), account.getIp());
                    auditRepository.save(audit);
                    sendRegistrationEmail(email, audit.getVerifyCode());
                }
            } else {
                throw new AccountRuntimeException(AccountMessage.REGISTRATION_EMAIL_EXISTS_ERROR);
            }

        }
    }

    private void sendRegistrationEmail(String to, String verifyCode) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(new InternetAddress(to));
            helper.setSubject("Verify Mini Diary Email Address");
            InternetAddress from = new InternetAddress(adminEmail, "Mini Diary Group");
            helper.setFrom(from);
            Map<String, Object> model = new HashMap<>();
            String url = "http://www.minidiary.org/sso/active/" + to + "/" + verifyCode;
            model.put("url", url);
            String text = VelocityEngineUtils.mergeTemplateIntoString(engine, "templates/registration.vm", "UTF-8",
                    model);
            helper.setText(text, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send out registration email to {}", to, e);
            throw new AccountRuntimeException(AccountMessage.REGISTRATION_ERROR_BY_EMAIL);
        }

        mailSender.send(message);
    }

    private void sendWelcomeEmail(String to) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(new InternetAddress(to));
            helper.setSubject("Welcome to Mini Diary");
            InternetAddress from = new InternetAddress(adminEmail, "Mini Diary Group");
            helper.setFrom(from);
            Map<String, Object> model = new HashMap<>();
            model.put("url", "http://www.minidiary.org");
            model.put("to", to);
            String text = VelocityEngineUtils.mergeTemplateIntoString(engine, "templates/welcome.vm", "UTF-8", model);
            helper.setText(text, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send out welcome email to {}", to, e);
        }

        mailSender.send(message);
    }

    @Override
    public void verify(AccountDTO account) {
        String email = account.getEmail();
        String verifyCode = account.getVerifyCode();

        Account oldAccount = accountRepository.findByEmail(email);
        if (oldAccount == null || !oldAccount.isPendingForActive()) {
            throw new AccountRuntimeException(AccountMessage.VERIFY_EMAIL_ERROR);
        } else {
            Audit oldAudit = auditRepository.findTopByAccountIdOrderByOperationDateDesc(oldAccount.getId());
            if (oldAudit.confirmVerifyCode(verifyCode)) {
                oldAccount.active();
                accountRepository.save(oldAccount);
                Audit newAudit = Audit.builder().accountId(oldAccount.getId()).build();
                newAudit.updateByOperationAndIp(AccountState.ACTIVE.name(), account.getIp());
                auditRepository.save(newAudit);
                try {
                    new Thread(() -> sendWelcomeEmail(email)).start();
                } catch (Exception e) {
                    log.warn("Failed to send welcome email. User: {}", email, e);
                }

            } else {
                throw new AccountRuntimeException(AccountMessage.VERIFY_EMAIL_ERROR);
            }
        }

    }

    @Override
    public AccountDTO login(AccountDTO account) {
        String email = account.getEmail();
        String password = account.getPassword();
        String ip = account.getIp();
        Account oldAccount = accountRepository.findByEmail(email);
        if (oldAccount == null || !oldAccount.isActive() || (!oldAccount.confirmPassword(password) && !verifyTemporaryPassword(oldAccount.getId(), password))) {
            throw new AccountRuntimeException(AccountMessage.LOGIN_ERROR);
        } else {
            Long id = oldAccount.getId();
            String token = TokenService.createToken(email, id);
            AccountDTO out = AccountDTO.builder().email(email).token(token).ip(ip).build();
            Audit audit = auditRepository.findTopByAccountIdAndOperationOrderByOperationDateDesc(id,
                    AccountAction.LOGIN.name());
            if (audit != null) {
                out.setLastLoginDate(audit.getOperationDate());
                out.setLastLoginIp(audit.getIp());
            }

            Audit newAudit = Audit.builder().accountId(oldAccount.getId()).build();
            newAudit.updateByOperationAndIp(AccountAction.LOGIN.name(), ip);
            auditRepository.save(newAudit);
            return out;
        }
    }

    @Override
    public void changePassword(AccountDTO account) {
        String password = account.getPassword();
        Long id = account.getId();
        Account oldAccount = accountRepository.findOne(id);
        if (oldAccount == null || !oldAccount.isActive()) {
            throw new AccountRuntimeException(AccountMessage.PASSWORD_UPDATE_ERROR);
        } else {
            if ( oldAccount.confirmPassword(password) || verifyTemporaryPassword(id, password)) {
                String newPassword = account.getNewPassword();
                oldAccount.modifyPassword(newPassword);
                accountRepository.save(oldAccount);
                Audit newAudit = Audit.builder().accountId(id).build();
                newAudit.updateByOperationAndIp(AccountAction.PASSWORD_CHANGE.name(), account.getIp());
                auditRepository.save(newAudit);
            }else {
                throw new AccountRuntimeException(AccountMessage.PASSWORD_IS_WRONG);
            }
           
            
            
        }

    }
    
    public boolean verifyTemporaryPassword(Long id, String password) {
        Audit forgetPassword = auditRepository.findTopByAccountIdAndOperationOrderByOperationDateDesc(id, AccountAction.PASSWORD_FORGET.name());
        if (forgetPassword != null && 
                forgetPassword.getOperationDate().plusHours(1L).isAfter(LocalDateTime.now()) &&
                forgetPassword.confirmVerifyCode(password)
                ) {
            return true;
         
        }else {
            return false;  
        }
      
        
    }
    
    
    
    @Override
    public void forgetPassword(AccountDTO account) {
        String email = account.getEmail();
        Account oldAccount = accountRepository.findByEmail(email);
        if (oldAccount == null) {
            throw new AccountRuntimeException(AccountMessage.EMAIL_REQUIRED);
        }
        
        if (oldAccount.isActive()) {
            Audit newAudit = Audit.builder().accountId(oldAccount.getId()).build();
            newAudit.generateVerifyCode();
            newAudit.updateByOperationAndIp(AccountAction.PASSWORD_FORGET.name(), account.getIp());
            
            String code = newAudit.getVerifyCode();
            newAudit.setVerifyCode(DigestUtils.sha256Hex(code));
            auditRepository.save(newAudit);
            sendForgetPasswordEmail(email,code );
        }else {
            throw new AccountRuntimeException(AccountMessage.ACCOUNT_NOT_ACTIVE);
        }
        
        
    }
    
    private void sendForgetPasswordEmail(String to, String password) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(new InternetAddress(to));
            helper.setSubject("Get my password back ");
            InternetAddress from = new InternetAddress(adminEmail, "Mini Diary Group");
            helper.setFrom(from);
            Map<String, Object> model = new HashMap<>();
            model.put("url", "http://www.minidiary.org");
            model.put("to", to);
            model.put("password", password);
            String text = VelocityEngineUtils.mergeTemplateIntoString(engine, "templates/forget-password.vm", "UTF-8", model);
            helper.setText(text, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send out welcome email to {}", to, e);
        }

        mailSender.send(message);
    }

}
