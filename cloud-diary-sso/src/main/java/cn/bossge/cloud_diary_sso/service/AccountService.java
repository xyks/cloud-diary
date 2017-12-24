package cn.bossge.cloud_diary_sso.service;

import cn.bossge.cloud_diary_sso.dto.AccountDTO;

public interface AccountService {

    void register(AccountDTO account);

    void verify(AccountDTO account);

    AccountDTO login(AccountDTO account);

    void changePassword(AccountDTO account);

    void forgetPassword(AccountDTO account);
}
