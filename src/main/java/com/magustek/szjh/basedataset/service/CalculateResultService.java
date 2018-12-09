package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.CalculateResult;

import java.util.List;

public interface CalculateResultService {
    List<CalculateResult> save(List<CalculateResult> list);
    List<CalculateResult> getAllByVersion(String version);
    void deleteAllByVersion(String version);
    List<CalculateResult> calculateByVersion(String version);
}
