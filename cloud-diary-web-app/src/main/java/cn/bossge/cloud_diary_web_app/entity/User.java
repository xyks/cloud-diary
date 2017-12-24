package cn.bossge.cloud_diary_web_app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "user" )
public class User {
    @Id
    @Column( name = "id" )
    private Long id;
    @Column( name = "nickname" )
    private String nickName;
    @Column( name = "signature" )
    private String signature;
    @Column( name = "gender" , columnDefinition = "Char")
    private String gender;
    @Column( name = "keycode")
    private String keyCode;
    @Column( name = "image_cover" )
    private String cover;
    @Column( name = "image_headportrait")
    private String headPortrait;
   

}
