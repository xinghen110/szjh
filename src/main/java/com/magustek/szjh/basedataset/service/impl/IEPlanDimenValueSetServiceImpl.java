package com.magustek.szjh.basedataset.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.magustek.szjh.basedataset.dao.IEPlanDimenValueSetDAO;
import com.magustek.szjh.basedataset.entity.IEPlanDimenValueSet;
import com.magustek.szjh.basedataset.entity.vo.IEPlanDimenValueSetVO;
import com.magustek.szjh.basedataset.service.IEPlanDimenValueSetService;
import com.magustek.szjh.configset.bean.IEPlanDimensionSet;
import com.magustek.szjh.configset.service.IEPlanDimensionSetService;
import com.magustek.szjh.configset.service.OrganizationSetService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.KeyValueBean;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.constant.IEPlanDimensionSetConstant;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("IEPlanDimenValueSetService")
public class IEPlanDimenValueSetServiceImpl implements IEPlanDimenValueSetService {

    private final HttpUtils httpUtils;
    private final IEPlanDimenValueSetDAO iePlanDimenValueSetDAO;
    private final IEPlanDimensionSetService iePlanDimensionSetService;
    private final OrganizationSetService organizationSetService;

    public IEPlanDimenValueSetServiceImpl(HttpUtils httpUtils, IEPlanDimenValueSetDAO iePlanDimenValueSetDAO, IEPlanDimensionSetService iePlanDimensionSetService, OrganizationSetService organizationSetService) {
        this.httpUtils = httpUtils;
        this.iePlanDimenValueSetDAO = iePlanDimenValueSetDAO;
        this.iePlanDimensionSetService = iePlanDimensionSetService;
        this.organizationSetService = organizationSetService;
    }

    @Override
    public List<IEPlanDimenValueSet> save(List<IEPlanDimenValueSet> list) {
        iePlanDimenValueSetDAO.save(list);
        return list;
    }

    @Override
    public List<IEPlanDimenValueSet> getAllByVersion(String version) {
        return Lists.newArrayList(iePlanDimenValueSetDAO.findAllByVersion(version));
    }

    @Transactional
    @Override
    public void deleteAllByVersion(String version) {
        iePlanDimenValueSetDAO.deleteAllByVersion(version);
    }

    public List<IEPlanDimenValueSet> getAllFromDatasource(String begin, String end, String bukrs) {
        List<IEPlanDimenValueSet> list = new ArrayList<>();
        //获取所有取数指标
        List<IEPlanDimensionSet> dimensionSetList = iePlanDimensionSetService.getAll();
        //根据取数指标循环取数
        dimensionSetList.forEach(dimensionSet -> list.addAll(getAllFromDatasource(begin,end,bukrs,dimensionSet)));
        return list;
    }

    @Transactional
    @Override
    public List<IEPlanDimenValueSet> fetchData() throws Exception {
        List<IEPlanDimenValueSet> list = new ArrayList<>();
        List<KeyValueBean> reportList = organizationSetService.getRangeList();
        reportList.parallelStream().forEach(item-> {
            try {
                list.addAll(getAllFromDatasource(item.getValue(), item.getOpera(), item.getKey()));
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        //清除今天的版本
        deleteAllByVersion(LocalDate.now().toString());
        //保存今天的新版本
        save(list);
        return list;
    }

    @Override
    public List<IEPlanDimenValueSetVO> getContractByHtsno(String htsno, String version) {
        if(Strings.isNullOrEmpty(version)){
            version = LocalDate.now().toString();
        }
        //获取指标列表（用来匹配指标值类型）
        Map<String, IEPlanDimensionSet> dimensionSetMap = iePlanDimensionSetService.getMappedList();

        //根据合同流水号及版本号返回所有单据
        List<IEPlanDimenValueSet> valueSetList = iePlanDimenValueSetDAO.findAllByHtsnoAndVersion(htsno, version);
        Map<String, List<IEPlanDimenValueSet>> collect = valueSetList
                .stream()
                .collect(Collectors.groupingBy(IEPlanDimenValueSet::getHtnum));
        List<IEPlanDimenValueSetVO> voList = new ArrayList<>();
        //根据htnum分组遍历
        collect.forEach((k, v)->{
            if(!ClassUtils.isEmpty(v)){
                IEPlanDimenValueSetVO vo = new IEPlanDimenValueSetVO();
                vo.setHtnum(k);
                vo.setHtsno(v.get(0).getHtsno());
                vo.setVersion(v.get(0).getVersion());
                List<KeyValueBean> kvList = new ArrayList<>();
                //组装指标信息
                v.forEach(i->{
                    KeyValueBean kv = new KeyValueBean();
                    kv.put(
                            i.getDmart(),//维度名称
                            dimensionSetMap.get(i.getDmart()).getDmnam(),//维度描述
                            i.getDmval());//维度值
                    kvList.add(kv);
                });
                vo.setDmList(kvList);
                voList.add(vo);
            }
        });
        return voList;
    }

    @Override
    public IEPlanDimenValueSet getDmvalByHtsno(String htsno, String dmart, String version) {
        return iePlanDimenValueSetDAO.findTopByHtsnoAndVersionAndDmart(htsno,version,dmart);
    }

    private List<IEPlanDimenValueSet> getAllFromDatasource(String begin, String end, String bukrs, IEPlanDimensionSet dimensionSet){
        String url = OdataUtils.IEPlanDimenValueSet
                +"?$filter=dmart eq '"+dimensionSet.getDmart()+"' " +
                "and bukrs eq '"+bukrs+"' " +
                "and begda ge datetime'"+begin+"T00:00:00' " +
                "and endda le datetime'"+end+"T23:59:59' ";
        String result = httpUtils.getResultByUrl(url, null, HttpMethod.GET);

        //如果指标返回类型是日期，则需要通过keys进行处理
        String[] keys = IEPlanDimensionSetConstant.RESULT_TYPE_DATS.equals(dimensionSet.getDmtyp()) ?
                new String[]{dimensionSet.getDmart()}:
                null;

        try {
            List<IEPlanDimenValueSet> list = OdataUtils.getListWithEntity(result,
                    IEPlanDimenValueSet.class,
                    keys,
                    "yyyy-MM-dd");

            list.forEach(item->{
                item.setDmval(item.getDmval().trim());
                item.setDmart(dimensionSet.getDmart());
                item.setBegda(begin);
                item.setEndda(end);
                item.setVersion(LocalDate.now().toString());
            });
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
