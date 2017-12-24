package cn.bossge.cloud_diary_web_app.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.bossge.cloud_diary_common.service.CustomDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryDTO {
	private String data;
	private Integer year;
	private Integer month;
	private Integer day;
	private Long accountId;
	@JsonSerialize (using = CustomDateSerializer.class)
	private LocalDateTime createdDate;
}
