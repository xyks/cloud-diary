package cn.bossge.cloud_diary_web_app.repository;

import org.springframework.data.repository.CrudRepository;

import cn.bossge.cloud_diary_web_app.entity.User;


public interface UserRepository extends CrudRepository<User, Long>{
}
