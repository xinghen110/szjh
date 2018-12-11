package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.CalculateResult;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface CalculateResultDAO extends CrudRepository<CalculateResult, Long> {
    @Transactional
    @Modifying
    void deleteAllByVersion(String version);
    List<CalculateResult> findAllByVersion(String version);
}
