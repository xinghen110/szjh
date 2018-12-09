package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.CalculateResult;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CalculateResultDAO extends CrudRepository<CalculateResult, Long> {
    void deleteAllByVersion(String version);
    List<CalculateResult> findAllByVersion(String version);
}
