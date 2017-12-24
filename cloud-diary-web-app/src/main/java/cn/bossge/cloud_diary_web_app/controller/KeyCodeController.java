package cn.bossge.cloud_diary_web_app.controller;

import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.bossge.cloud_diary_common.dto.ResponseDTO;
import cn.bossge.cloud_diary_common.service.TokenService;
import cn.bossge.cloud_diary_web_app.constant.DiaryMessage;
import cn.bossge.cloud_diary_web_app.service.UserService;

@RestController
@Log4j2
public class KeyCodeController {


    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping("keycode")
    public ResponseDTO checkKeyCode(@RequestBody String keyCode, @RequestHeader String token){
        Claims body = TokenService.parseToken(token);
        int result = userService.checkKeyCode(keyCode, Long.valueOf(body.getId()));
        String message;
        if (result == 0) {
            message = DiaryMessage.USER_KEY_CODE_SUCCESS;
        }else if (result > 0) {
            message = DiaryMessage.USER_KEY_CODE_CONFIRM;
        }else {
            message = DiaryMessage.USER_KEY_CODE_ERROR;
        }
        
        return  ResponseDTO.builder().success(true).message(message).code(result).build();
    }
    
    @CrossOrigin
    @PostMapping("savekeycode")
    public ResponseDTO saveKeyCode(@RequestBody String keyCode, @RequestHeader String token){
        Claims body = TokenService.parseToken(token);
        userService.saveKeyCode(keyCode, Long.valueOf(body.getId()));
        return  ResponseDTO.success(DiaryMessage.USER_KEY_CODE_SAVE_SUCCESS);
    }
    
    
    
    
}
