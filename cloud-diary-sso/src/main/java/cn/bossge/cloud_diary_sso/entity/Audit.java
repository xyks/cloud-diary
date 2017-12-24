package cn.bossge.cloud_diary_sso.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "accountid")
    private Long accountId;
    @Column(name = "operation")
    private String operation;
    @Column(name = "verifycode")
    private String verifyCode;
    @Column(name = "operationdate")
    private LocalDateTime operationDate;
    @Column(name = "ip")
    private String ip;

    public void updateByOperationAndIp(String operation, String ip) {
        LocalDateTime now = LocalDateTime.now();
        this.setOperation(operation);
        this.setOperationDate(now);
        this.setIp(ip);
    }

    ;

    public void generateVerifyCode() {
        this.setVerifyCode(RandomStringUtils.randomAlphabetic(20));
    }

    public boolean confirmVerifyCode(String code) {
        if (StringUtils.isEmpty(this.verifyCode)) {
            return false;
        } else {
            return this.verifyCode.equals(code);
        }
    }
}
