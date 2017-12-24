
package cn.bossge.cloud_diary_sso.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.bossge.cloud_diary_common.service.CustomDateSerializer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDTO {
	private Long id;
    private String email;
    private String password;
    private String ip;
    private String verifyCode;
    private String newPassword;
    private String lastLoginIp;
    @JsonSerialize (using = CustomDateSerializer.class)
    private LocalDateTime lastLoginDate;
    private String token;
}
