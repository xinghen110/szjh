package com.magustek.szjh.approval.dao;

import com.magustek.szjh.approval.bean.ApprovalLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApprovalLogDAO extends CrudRepository<ApprovalLog, Long> {
    ApprovalLog save(ApprovalLog approvalLog);
    List<ApprovalLog> findAllByHeaderIdOrderByIdAsc(Long headerId);
}
