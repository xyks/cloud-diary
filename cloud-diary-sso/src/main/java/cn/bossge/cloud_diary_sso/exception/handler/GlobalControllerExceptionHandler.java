package cn.bossge.cloud_diary_sso.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.bossge.cloud_diary_common.dto.ResponseDTO;
import cn.bossge.cloud_diary_common.exception.TokenException;
import cn.bossge.cloud_diary_sso.exception.AccountRuntimeException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler({AccountRuntimeException.class,TokenException.class})
    public ResponseDTO accountError(Exception exception) {
        return ResponseDTO.error(exception.getMessage());
    }


}