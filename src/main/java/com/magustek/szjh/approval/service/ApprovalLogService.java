package com.magustek.szjh.approval.service;

import com.magustek.szjh.approval.bean.ApprovalLog;

import java.util.List;

public interface ApprovalLogService {
    List<ApprovalLog> getAllLogsByHeaderid(Long headerid);
}
