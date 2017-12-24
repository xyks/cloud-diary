package cn.bossge.cloud_diary_web_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import cn.bossge.cloud_diary_common.dto.ResponseDTO;
import cn.bossge.cloud_diary_common.service.TokenService;
import cn.bossge.cloud_diary_web_app.constant.DiaryMessage;
import cn.bossge.cloud_diary_web_app.dto.DiaryDTO;
import cn.bossge.cloud_diary_web_app.service.DiaryService;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class DiaryController {
	
	@Autowired
	DiaryService diaryService;
	
	@CrossOrigin
	@PostMapping("diary")
	public ResponseDTO writeDiary(@RequestBody DiaryDTO diary, @RequestHeader String token){
	    Claims body = TokenService.parseToken(token);
		diary.setAccountId(Long.valueOf(body.getId()));
		diaryService.write(diary);	
		ResponseDTO response = ResponseDTO.builder().success(true).message(DiaryMessage.DIARY_SAVE_SUCCESS).content(diary).build();
		return response;
	}
	
	@CrossOrigin
	@GetMapping("diary/{year}/{month}/{day}")
	public ResponseDTO getDiary(DiaryDTO diary, @RequestHeader String token){
	    Claims body = TokenService.parseToken(token);
		diary.setAccountId(Long.valueOf(body.getId()));
		DiaryDTO result = diaryService.get(diary);	
		ResponseDTO response = ResponseDTO.builder().success(true).content(result).message(DiaryMessage.DIARY_GET_SUCCESS).build();
		return response;
	}




}
