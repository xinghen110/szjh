package com.magustek.szjh.basedataset.service;

import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.basedataset.entity.vo.IEPlanSelectValueSetVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IEPlanSelectValueSetService {
    List<IEPlanSelectValueSet> save(List<IEPlanSelectValueSet> list);
    List<IEPlanSelectValueSet> getAllByVersion(String version);
    void deleteAllByVersion(String version);
    List<IEPlanSelectValueSet> fetchData() throws Exception;
    List<IEPlanSelectValueSetVO> getContractByHtsnoAndVersionGroupByHtnum(String htsno, String version) throws Exception;

    List<IEPlanSelectValueSet> getAllByVersionAndPflag(String version, String pflag);
    List<IEPlanSelectValueSet> getContractByHtsnoAndVersion(String htsno, String version);
    List<IEPlanSelectValueSet> getContractByHtsnoSetAndVersion(Set<String> htsnoSet, String version);

    int updateReferencedByVersion(String referenced, String version);

    List<IEPlanSelectValueSet> getAllByVersionAndSdvarIn(String version, String serch, List<String> sdvarList);
}
