package cn.bossge.cloud_diary_web_app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryId implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column( name = "accountid" )
	private Long accountId;
	@Column( name = "year", columnDefinition = "SMALLINT" )
	private Integer year;
	@Column( name = "month", columnDefinition = "TINYINT" )
	private Integer month;
	@Column( name = "day", columnDefinition = "TINYINT" )
	private Integer day;
}
