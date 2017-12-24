package cn.bossge.cloud_diary_web_app.exception.handler;

import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.bossge.cloud_diary_common.dto.ResponseDTO;
import cn.bossge.cloud_diary_web_app.exception.UserException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(SignatureException.class)
    public ResponseDTO tokenError(SignatureException e){
        return ResponseDTO.builder().success(false).message(e.getMessage()).build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UserException.class)
    public ResponseDTO UserInfoError(UserException e){
        return ResponseDTO.builder().success(false).message(e.getMessage()).build();
    }
}
