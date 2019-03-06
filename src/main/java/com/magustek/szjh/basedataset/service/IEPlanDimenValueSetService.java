package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.IEPlanDimenValueSet;
import com.magustek.szjh.basedataset.entity.vo.IEPlanDimenValueSetVO;

import java.util.List;

public interface IEPlanDimenValueSetService {
    List<IEPlanDimenValueSet> save(List<IEPlanDimenValueSet> list);
    List<IEPlanDimenValueSet> getAllByVersion(String version);
    void deleteAllByVersion(String version);
    //List<IEPlanDimenValueSet> getAllFromDatasource(String begin, String end, String bukrs) throws Exception;
    List<IEPlanDimenValueSet> fetchData() throws Exception;

    List<IEPlanDimenValueSetVO> getContractByHtsno(String htsno, String version) throws Exception;
    IEPlanDimenValueSet getDmvalByHtsno(String htsno, String dmart, String version) throws Exception;
    List<IEPlanDimenValueSet> getDmvalByDmartAndVersion(String dmart, String version);
}
