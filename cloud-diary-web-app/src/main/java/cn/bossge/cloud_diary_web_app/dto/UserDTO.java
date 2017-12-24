package cn.bossge.cloud_diary_web_app.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String nickName;
    private String signature;
    private String gender;
    private String cover;
    private String headPortrait;
}
