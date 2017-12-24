package cn.bossge.cloud_diary_common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {
	private boolean success;
	private int code;
	private String message;
	private Object content;
	public static ResponseDTO success(String message) {
		return success(message, null);
	}
	public static ResponseDTO success(String message, Object content) {
		return ResponseDTO.builder().success(true).message(message).content(content).build();
	}
	
	public static ResponseDTO error(String message) {
		return error(message, null);
	}
	public static ResponseDTO error(String message, Object content) {
		return ResponseDTO.builder().success(false).message(message).content(content).build();
	}

	
}
