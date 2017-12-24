package cn.bossge.cloud_diary_sso.repository;

import org.springframework.data.repository.CrudRepository;

import cn.bossge.cloud_diary_sso.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findByEmail(String email);
}
