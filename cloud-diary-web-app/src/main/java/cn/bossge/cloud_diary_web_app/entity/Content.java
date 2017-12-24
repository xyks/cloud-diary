package cn.bossge.cloud_diary_web_app.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "content" )
public class Content {
	@Id
	@GeneratedValue
	@Column( name = "id" )
	private Long id;
	@Column( name = "data", columnDefinition = "text" )
	private String data;
	@Column( name = "createddate" )
	private LocalDateTime createdDate;
}
