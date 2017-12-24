package cn.bossge.cloud_diary_sso.repository;

import org.springframework.data.repository.CrudRepository;

import cn.bossge.cloud_diary_sso.entity.Audit;

public interface AuditRepository extends CrudRepository<Audit, Long> {
    Audit findTopByAccountIdOrderByOperationDateDesc(Long accountId);

    Audit findTopByAccountIdAndOperationOrderByOperationDateDesc(Long accountId, String operation);

}
