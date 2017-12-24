package cn.bossge.cloud_diary_web_app.service;

import cn.bossge.cloud_diary_web_app.dto.DiaryDTO;

public interface DiaryService {

	void write(DiaryDTO diary);

	DiaryDTO get(DiaryDTO diary);
	
}
