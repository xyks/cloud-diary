package cn.bossge.cloud_diary_sso.controller;

import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import cn.bossge.cloud_diary_common.dto.ResponseDTO;
import cn.bossge.cloud_diary_common.service.TokenService;
import cn.bossge.cloud_diary_sso.constant.AccountMessage;
import cn.bossge.cloud_diary_sso.dto.AccountDTO;
import cn.bossge.cloud_diary_sso.exception.AccountRuntimeException;
import cn.bossge.cloud_diary_sso.service.AccountService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IndexController {
    @Autowired
    AccountService accountService;

    @GetMapping("/")
    public String index() {
        return "App is running. "+LocalDateTime.now();
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseDTO register(@RequestBody AccountDTO account, HttpServletRequest request) {
        if (StringUtils.isEmpty(account.getEmail()) || StringUtils.isEmpty(account.getPassword())) {
            throw new AccountRuntimeException(AccountMessage.REGISTRATION_ERROR);
        }
        String ip = request.getRemoteAddr();
        account.setIp(ip);
        try {
            accountService.register(account);
        } catch (Exception e) {
            throw new AccountRuntimeException(e.getMessage());
        }
         
        return ResponseDTO.success(AccountMessage.REGISTRATION_SUCCESS);
    }

    @CrossOrigin
    @GetMapping("/active/{email}/{verifyCode}")
    public String verifyEmail(@PathVariable String email, @PathVariable String verifyCode, HttpServletRequest request) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(verifyCode)) {
            return AccountMessage.VERIFY_EMAIL_ERROR;
        }
        String ip = request.getRemoteAddr();
        AccountDTO account = AccountDTO.builder().email(email).verifyCode(verifyCode).ip(ip).build();
        try {
            accountService.verify(account);
        } catch (Exception e) {
            return e.getMessage();
        }
         return AccountMessage.VERIFY_EMAIL_SUCCESS;
    }


    @CrossOrigin
    @PostMapping("/login")
    public ResponseDTO login(@RequestBody AccountDTO account, HttpServletRequest request) {
        if (StringUtils.isEmpty(account.getEmail()) || StringUtils.isEmpty(account.getPassword())) {
            throw new AccountRuntimeException(AccountMessage.LOGIN_ERROR);
        }

        String ip = request.getRemoteAddr();
        account.setIp(ip);
        AccountDTO result = null;
        try {
            result = accountService.login(account);
        } catch (Exception e) {
            throw new AccountRuntimeException(e.getMessage());
        }
          return ResponseDTO.success(AccountMessage.LOGIN_SUCCESS,result);
       
    }

    @CrossOrigin
    @PostMapping("/password")
    public ResponseDTO login(@RequestBody AccountDTO account, HttpServletRequest request, @RequestHeader String token) {
        if (StringUtils.isEmpty(account.getPassword()) || StringUtils.isEmpty(account.getNewPassword())) {
            throw new AccountRuntimeException(AccountMessage.PASSWORD_UPDATE_ERROR);
        }
        Claims body = TokenService.parseToken(token);
        String ip = request.getRemoteAddr();
        account.setIp(ip);
        account.setId(Long.valueOf(body.getId()));
        account.setEmail(body.getSubject());
        
        //TODO remove later, only for beta test
        if("a@a.com".equalsIgnoreCase(body.getSubject())) {
            throw new AccountRuntimeException("The password of testing account can not be changed.");
        }
        
        try {
            accountService.changePassword(account);
        } catch (Exception e) {
            throw new AccountRuntimeException(e.getMessage());
        }
        return ResponseDTO.success(AccountMessage.PASSWORD_UPDATE_SUCCESS);
       
    }
    
    @CrossOrigin
    @PostMapping("/forgetpassword")
    public ResponseDTO forgetPassword(@RequestBody AccountDTO account, HttpServletRequest request) {
        if (StringUtils.isEmpty(account.getEmail())) {
            throw new AccountRuntimeException(AccountMessage.EMAIL_REQUIRED);
        }
        String ip = request.getRemoteAddr();
        account.setIp(ip);
        accountService.forgetPassword(account);
        return ResponseDTO.success(AccountMessage.PASSWORD_TEMP_SUCCESS);
       
    }

}
