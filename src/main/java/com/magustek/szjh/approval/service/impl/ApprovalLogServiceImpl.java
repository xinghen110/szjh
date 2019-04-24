package com.magustek.szjh.approval.service.impl;

import com.magustek.szjh.approval.bean.ApprovalLog;
import com.magustek.szjh.approval.dao.ApprovalLogDAO;
import com.magustek.szjh.approval.service.ApprovalLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ApprovalLogServiceImpl")
public class ApprovalLogServiceImpl implements ApprovalLogService {
    private final ApprovalLogDAO approvalLogDao;

    public ApprovalLogServiceImpl(ApprovalLogDAO approvalLogDao) {
        this.approvalLogDao = approvalLogDao;
    }

    @Override
    public List<ApprovalLog> getAllLogsByHeaderId(Long headerId) {
        return approvalLogDao.findAllByHeaderIdOrderByIdAsc(headerId);
    }

}
