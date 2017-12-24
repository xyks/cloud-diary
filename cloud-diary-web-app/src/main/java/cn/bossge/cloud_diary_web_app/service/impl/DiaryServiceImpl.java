package cn.bossge.cloud_diary_web_app.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bossge.cloud_diary_web_app.dto.DiaryDTO;
import cn.bossge.cloud_diary_web_app.entity.Content;
import cn.bossge.cloud_diary_web_app.entity.Diary;
import cn.bossge.cloud_diary_web_app.entity.DiaryId;
import cn.bossge.cloud_diary_web_app.repository.DiaryRepository;
import cn.bossge.cloud_diary_web_app.service.DiaryService;

@Service
@Transactional
public class DiaryServiceImpl implements DiaryService {

	@Autowired
	DiaryRepository diaryRepository;
	@Override
	public void write(DiaryDTO diary) {
		Content content = Content.builder().data(diary.getData()).createdDate(LocalDateTime.now()).build();
		DiaryId id = DiaryId.builder().accountId(diary.getAccountId()).year(diary.getYear()).month(diary.getMonth()).day(diary.getDay()).build();
		Diary oldDiary = diaryRepository.findOne(id);
		if (oldDiary == null) {
			Diary newDiary = Diary.builder().id(id).content(content).build();
			diaryRepository.save(newDiary);
		}else{
			oldDiary.setContent(content);
			diaryRepository.save(oldDiary);
		}
	}
	@Override
	public DiaryDTO get(DiaryDTO diary) {
		DiaryId id = DiaryId.builder().accountId(diary.getAccountId()).year(diary.getYear()).month(diary.getMonth()).day(diary.getDay()).build();
		Diary wanted = diaryRepository.findOne(id);
		if (wanted == null) {
			return null;
		}
		DiaryDTO result = new DiaryDTO();
		Content content = wanted.getContent();
		result.setData(content.getData());
		result.setCreatedDate(content.getCreatedDate());
		return result;
	}

}
