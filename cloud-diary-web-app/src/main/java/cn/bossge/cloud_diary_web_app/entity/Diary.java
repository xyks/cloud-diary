package cn.bossge.cloud_diary_web_app.entity;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "diary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {
	
	@EmbeddedId
	private DiaryId id;
	@OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
	@JoinColumn( name = "contentid" )
	private Content content;

}
