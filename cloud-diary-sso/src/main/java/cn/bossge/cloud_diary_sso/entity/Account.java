package cn.bossge.cloud_diary_sso.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.util.StringUtils;

import cn.bossge.cloud_diary_sso.enums.AccountState;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Setter;

@Entity
@Table(name = "account")
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "salt")
    private String salt;
    @Column(name = "state")
    private String state;
    @Column(name = "createddate")
    private LocalDateTime createdDate;
    @Column(name = "lastmodifieddate")
    private LocalDateTime lastModifiedDate;

    public void createByEmailAndPassword(String email, String password) {
        LocalDateTime now = LocalDateTime.now();
        this.setEmail(email);
        this.setCreatedDate(now);
        this.setLastModifiedDate(now);
        this.setState(AccountState.PENDING_FOR_ACTIVE.name());
        this.generatePassword(password);
    }

    public void modifyPassword(String password) {
        LocalDateTime now = LocalDateTime.now();
        this.setLastModifiedDate(now);
        this.generatePassword(password);
    }

    private void generatePassword(String password) {
        this.salt = RandomStringUtils.randomAscii(128);
        String hashedPassword = generateHashedPassword(password);
        this.setPassword(hashedPassword);
    }

    private String generateHashedPassword(String origin) {
        String max = origin + salt;
        return DigestUtils.sha256Hex(max);
    }

    public boolean isPendingForActive() {
        if (StringUtils.isEmpty(this.state)) {
            return false;
        } else {
            AccountState accountState = AccountState.valueOf(this.state);
            return AccountState.PENDING_FOR_ACTIVE.equals(accountState);
        }
    }

    public boolean isActive() {
        if (StringUtils.isEmpty(this.state)) {
            return false;
        } else {
            AccountState accountState = AccountState.valueOf(this.state);
            return AccountState.ACTIVE.equals(accountState);
        }
    }

    public boolean confirmPassword(String origin) {
        if (StringUtils.isEmpty(this.password) || StringUtils.isEmpty(this.salt)) {
            return false;
        } else {
            String expected = generateHashedPassword(origin);
            return password.equals(expected);
        }
    }


    public void active() {
        this.setState(AccountState.ACTIVE.name());
        this.setLastModifiedDate(LocalDateTime.now());
    }
}
