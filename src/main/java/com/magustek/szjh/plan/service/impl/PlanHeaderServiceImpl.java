package com.magustek.szjh.plan.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.magustek.szjh.configset.bean.IEPlanDimensionSet;
import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.configset.service.ConfigDataSourceSetService;
import com.magustek.szjh.configset.service.IEPlanReportHeadSetService;
import com.magustek.szjh.configset.service.OrganizationSetService;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.PlanItem;
import com.magustek.szjh.plan.bean.PlanLayout;
import com.magustek.szjh.plan.bean.vo.PlanHeaderVO;
import com.magustek.szjh.plan.dao.PlanHeaderDAO;
import com.magustek.szjh.plan.dao.PlanLayoutDAO;
import com.magustek.szjh.plan.service.PlanHeaderService;
import com.magustek.szjh.plan.service.PlanItemService;
import com.magustek.szjh.plan.service.RollPlanArchiveService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.KeyValueBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("PlanHeaderService")
public class PlanHeaderServiceImpl implements PlanHeaderService {

    private PlanHeaderDAO planHeaderDAO;
    private PlanItemService planItemService;
    private OrganizationSetService organizationSetService;
    private ConfigDataSourceSetService configDataSourceSetService;
    private IEPlanReportHeadSetService iePlanReportHeadSetService;
    private PlanLayoutDAO planLayoutDAO;
    private RollPlanArchiveService rollPlanArchiveService;

    public PlanHeaderServiceImpl(PlanHeaderDAO planHeaderDAO, PlanItemService planItemService, OrganizationSetService organizationSetService, ConfigDataSourceSetService configDataSourceSetService, IEPlanReportHeadSetService iePlanReportHeadSetService, PlanLayoutDAO planLayoutDAO, RollPlanArchiveService rollPlanArchiveService) {
        this.planHeaderDAO = planHeaderDAO;
        this.planItemService = planItemService;
        this.organizationSetService = organizationSetService;
        this.configDataSourceSetService = configDataSourceSetService;
        this.iePlanReportHeadSetService = iePlanReportHeadSetService;
        this.planLayoutDAO = planLayoutDAO;
        this.rollPlanArchiveService = rollPlanArchiveService;
    }

    @Override
    public PlanHeader save(PlanHeader header) throws Exception{
        if(Strings.isNullOrEmpty(header.getBukrs())){
            header.setBukrs(ContextUtils.getCompany().getOrgcode());
            //公司报表
            if(IEPlanDimensionSet.DM_Company.equals(header.getRporg())){
                header.setOrgval(ContextUtils.getCompany().getOrgcode());
            }
            //部门报表
            if(IEPlanDimensionSet.DM_Department.equals(header.getRporg())){
                header.setOrgval(ContextUtils.getCompany().getDeptcode());
            }
        }
        //新增计划，初始化相关数据。
        if(ClassUtils.isEmpty(header.getId())){
            header.setStonr("10");//如果没有ID，默认初始值为10-创建
            header.setBsta("J01");//如果没有ID，默认初始值为J01
            //保存抬头
            header = planHeaderDAO.save(header);
            //获取计划布局配置
            IEPlanReportHeadVO config = iePlanReportHeadSetService.getReportConfigByBukrs(header.getBukrs(), header.getRptyp(), header.getRporg(), header.getJhval());
            //初始化明细数据
            List<PlanItem> itemList = planItemService.initItemDataByConfig(config, header.getId());
            //如果是月报，需要复制数据到【roll_plan_head_data_archive】、【roll_plan_item_data_archive】表，并且将统计数据存入其中
            if("MR".equals(header.getRptyp())){
                planItemService.initCalcData(itemList, config, header);
            }
            //保存明细数据
            planItemService.save(itemList);
            //保存布局数据
            PlanLayout layout = new PlanLayout();
            layout.setHeaderId(header.getId());
            layout.setLayout(config.toJson());
            planLayoutDAO.save(layout);

        }else{
            PlanHeader old = planHeaderDAO.findOne(header.getId());
            if(old != null){
                header.copyCreate(old);
            }
            header = planHeaderDAO.save(header);
        }
        return header;
    }

    @Transactional
    @Override
    public PlanHeader delete(PlanHeader header) {
        Assert.isTrue(!ClassUtils.isEmpty(header.getId()), "计划ID不得为空");
        PlanHeader one = planHeaderDAO.findOne(header.getId());
        //级联删除
        planItemService.deleteByHeaderId(header.getId());
        planHeaderDAO.delete(header.getId());
        planLayoutDAO.deleteAllByHeaderId(header.getId());

        if("MR".equals(one.getRptyp())){
            //月报要删除滚动计划归档数据
            rollPlanArchiveService.deleteData(one);
        }

        return header;
    }

    @Override
    public PlanHeader getById(PlanHeader header) throws Exception{
        Assert.isTrue(!ClassUtils.isEmpty(header.getId()), "计划ID不得为空");
        header = planHeaderDAO.findOne(header.getId());
        return coverToVO(header);
    }

    @SuppressWarnings("unused")
    @Override
    public Page<Map<String, String>> getListByBukrs(PlanHeaderVO vo, Pageable pageable) throws Exception{
        Page<PlanHeader> page;
        switch (vo.getRporg()) {
            case IEPlanDimensionSet.DM_Company:
                page = planHeaderDAO.findAllByBukrsAndOrgvalAndRptypOrderByIdDesc(ContextUtils.getCompany().getOrgcode(), ContextUtils.getCompany().getOrgcode(), vo.getRptyp(), pageable);
                break;
            case IEPlanDimensionSet.DM_Department:
                page = planHeaderDAO.findAllByBukrsAndOrgvalAndRptypOrderByIdDesc(ContextUtils.getCompany().getOrgcode(), ContextUtils.getCompany().getDeptcode(), vo.getRptyp(), pageable);
                break;
            default:
                throw new Exception("请指定公司报表还是部门报表！");
        }

        List<PlanHeader> content = page.getContent();
        List<Map<String, String>> voList = new ArrayList<>(content.size());

        for(PlanHeader header : content){
            PlanHeaderVO pvo = coverToVO(header);
            pvo.setZbList(planItemService.getZbList(pvo.getId()));
            voList.add(ClassUtils.coverToMapJson(pvo,"zbList"));
        }
        page.getContent();
        return new PageImpl<>(voList, pageable, page.getTotalElements());
    }

    @Override
    public IEPlanReportHeadVO getLayoutByHeaderId(Long headerId) {
        return JSON.parseObject(planLayoutDAO.findTopByHeaderId(headerId).getLayout(), IEPlanReportHeadVO.class);
    }

    private PlanHeaderVO coverToVO(PlanHeader header) throws Exception{
        Map<String, BigDecimal> zbval = planItemService.getZBValByHeaderId(header.getId());
        PlanHeaderVO vo = new PlanHeaderVO();
        BeanUtils.copyProperties(header, vo);
        ArrayList<KeyValueBean> keyValueBeans = KeyValueBean.paresMap(zbval);
        vo.setZbList(keyValueBeans);
        //公司代码描述
        vo.setButxt(organizationSetService.getByBukrs(vo.getBukrs()).getButxt());
        //审批状态描述
        vo.setSptxt(configDataSourceSetService.getDescByQcgrpAndQcode("STON", vo.getStonr()));
        //业务状态描述
        vo.setBstxt(configDataSourceSetService.getDescByQcgrpAndQcode("BSTA", vo.getBsta()));
        //报表类型描述
        vo.setRptxt(configDataSourceSetService.getDescByQcgrpAndQcode("RTYP", vo.getRptyp()));
        //货币名称
        vo.setKtext(configDataSourceSetService.getDescByQcgrpAndQcode("WAER", vo.getWaers()));
        //货币单位描述
        vo.setUnitx(configDataSourceSetService.getDescByQcgrpAndQcode("ZBUN", vo.getUnit()));
        return vo;
    }

}
