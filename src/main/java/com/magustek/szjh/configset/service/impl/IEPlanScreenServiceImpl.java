package com.magustek.szjh.configset.service.impl;

import com.magustek.szjh.configset.bean.IEPlanScreenHeadSet;
import com.magustek.szjh.configset.bean.IEPlanScreenItemSet;
import com.magustek.szjh.configset.bean.vo.IEPlanScreenVO;
import com.magustek.szjh.configset.dao.IEPlanScreenHeadSetDAO;
import com.magustek.szjh.configset.dao.IEPlanScreenItemSetDAO;
import com.magustek.szjh.configset.service.IEPlanScreenService;
import com.magustek.szjh.utils.OdataUtils;
import com.magustek.szjh.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("IEPlanScreenService")
public class IEPlanScreenServiceImpl implements IEPlanScreenService {

    private IEPlanScreenHeadSetDAO iePlanScreenHeadSetDAO;
    private IEPlanScreenItemSetDAO iePlanScreenItemSetDAO;
    private final HttpUtils httpUtils;

    public IEPlanScreenServiceImpl(IEPlanScreenHeadSetDAO iePlanScreenHeadSetDAO, IEPlanScreenItemSetDAO iePlanScreenItemSetDAO, HttpUtils httpUtils) {
        this.iePlanScreenHeadSetDAO = iePlanScreenHeadSetDAO;
        this.iePlanScreenItemSetDAO = iePlanScreenItemSetDAO;
        this.httpUtils = httpUtils;
    }

    @Override
    public List<IEPlanScreenHeadSet> saveHead(List<IEPlanScreenHeadSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0) {
            iePlanScreenHeadSetDAO.save(list);
        }else{
            log.error("IEPlanScreenHeadSet 数据为空！");
        }
        return list;
    }

    @Override
    public List<IEPlanScreenItemSet> saveItem(List<IEPlanScreenItemSet> list) {
        list.removeIf(item-> !item.getMsgtype().equals("S"));
        if(list.size()>0) {
            iePlanScreenItemSetDAO.save(list);
        }else{
            log.error("IEPlanScreenItemSet 数据为空！");
        }
        return list;
    }

    @Override
    public IEPlanScreenVO findHeadByBukrsAndRptypAndHview(String bukrs, String rptyp, String hview) {
        IEPlanScreenVO vo = new IEPlanScreenVO();
        IEPlanScreenHeadSet head = iePlanScreenHeadSetDAO.findTopByBukrsAndRptypAndHview(bukrs, rptyp, hview);
        BeanUtils.copyProperties(head, vo);
        vo.setItemSetList(iePlanScreenItemSetDAO.findAllByHdnumOrderByZsort(head.getHdnum()));
        return vo;
    }

    @Override
    public void deleteAll() {
        iePlanScreenHeadSetDAO.deleteAll();
        iePlanScreenItemSetDAO.deleteAll();
    }

    @Override
    public void getAllFromDatasource() throws Exception {
        String headResult = httpUtils.getResultByUrl(OdataUtils.IEPlanScreenHeadSet+"?", null, HttpMethod.GET);
        List<IEPlanScreenHeadSet> headList = OdataUtils.getListWithEntity(headResult, IEPlanScreenHeadSet.class);

        String itemResult = httpUtils.getResultByUrl(OdataUtils.IEPlanScreenItemSet+"?", null, HttpMethod.GET);
        List<IEPlanScreenItemSet> itemList = OdataUtils.getListWithEntity(itemResult, IEPlanScreenItemSet.class);
        //清除现有数据
        deleteAll();
        //保存新数据
        saveHead(headList);
        saveItem(itemList);
    }

    @Override
    public List<IEPlanScreenItemSet> getItemListByIntfa(String intfa) {
        return iePlanScreenItemSetDAO.findAllByIntfa(intfa);
    }
}
