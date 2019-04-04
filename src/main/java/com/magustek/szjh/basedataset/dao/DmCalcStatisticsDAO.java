package com.magustek.szjh.basedataset.dao;

import com.magustek.szjh.basedataset.entity.DmCalcStatistics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DmCalcStatisticsDAO extends CrudRepository<DmCalcStatistics, Long> {
    void deleteAllByVersion(String version);
    DmCalcStatistics findFirstByVersionAndDmartAndDmvalAndCaart(String version, String dmart, String dmval, String caart);

    List<DmCalcStatistics> findAllByDmartAndVersion(String dmart, String version);
    List<DmCalcStatistics> findAllByDmartAndCaartAndVersion(String dmart, String caart, String version);

    List<DmCalcStatistics> findAllByVersionInAndDmvalInAndAndCaartInOrderByVersion(String[] versionList, String[] dmvalList, String[] caart);
}
