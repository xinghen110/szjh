package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.DmCalcStatistics;
import org.springframework.data.repository.CrudRepository;

public interface DmCalcStatisticsDAO extends CrudRepository<DmCalcStatistics, Long> {
    void deleteAllByVersion(String version);
}
