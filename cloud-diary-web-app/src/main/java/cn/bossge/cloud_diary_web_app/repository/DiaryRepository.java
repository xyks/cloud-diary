package cn.bossge.cloud_diary_web_app.repository;

import org.springframework.data.repository.CrudRepository;

import cn.bossge.cloud_diary_web_app.entity.Diary;
import cn.bossge.cloud_diary_web_app.entity.DiaryId;

public interface DiaryRepository extends CrudRepository<Diary, DiaryId>{
	
}
