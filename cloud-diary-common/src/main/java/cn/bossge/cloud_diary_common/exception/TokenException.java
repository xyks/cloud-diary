package cn.bossge.cloud_diary_common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenException extends RuntimeException {

	private String message;	
}
