package com.magustek.szjh.configset.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.magustek.szjh.configset.bean.*;
import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.configset.bean.vo.IEPlanScreenVO;
import com.magustek.szjh.configset.service.*;
import com.magustek.szjh.user.bean.UserInfo;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.KeyValueBean;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 配置表
 * */
@Api("配置表")
@Slf4j
@RestController
@RequestMapping(value = "/IEPlanConfigSet", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class IEPlanConfigSetController {

    private IEPlanOperationSetService iePlanOperationSetService;
    private IEPlanCalculationSetService iePlanCalculationSetService;
    private IEPlanDimensionSetService iePlanDimensionSetService;
    private IEPlanSelectDataSetService iePlanSelectDataSetService;
    private ConfigDataSourceSetService configDataSourceSetService;
    private OrganizationSetService organizationSetService;
    private IEPlanReportHeadSetService iePlanReportHeadSetService;
    private IEPlanReportItemSetService iePlanReportItemSetService;
    private IEPlanStatisticSetService iePlanStatisticSetService;
    private IEPlanBusinessHeadSetService iePlanBusinessHeadSetService;
    private IEPlanBusinessItemSetService iePlanBusinessItemSetService;
    private IEPlanScreenService iePlanScreenService;
    private BaseResponse resp;

    public IEPlanConfigSetController(IEPlanOperationSetService iePlanOperationSetService, IEPlanCalculationSetService iePlanCalculationSetService, IEPlanDimensionSetService iePlanDimensionSetService, IEPlanSelectDataSetService iePlanSelectDataSetService, ConfigDataSourceSetService configDataSourceSetService, OrganizationSetService organizationSetService, IEPlanReportHeadSetService iePlanReportHeadSetService, IEPlanReportItemSetService iePlanReportItemSetService, IEPlanStatisticSetService iePlanStatisticSetService, IEPlanBusinessHeadSetService iePlanBusinessHeadSetService, IEPlanBusinessItemSetService iePlanBusinessItemSetService, IEPlanScreenService iePlanScreenService) {
        this.iePlanOperationSetService = iePlanOperationSetService;
        this.iePlanCalculationSetService = iePlanCalculationSetService;
        this.iePlanDimensionSetService = iePlanDimensionSetService;
        this.iePlanSelectDataSetService = iePlanSelectDataSetService;
        this.configDataSourceSetService = configDataSourceSetService;
        this.organizationSetService = organizationSetService;
        this.iePlanReportHeadSetService = iePlanReportHeadSetService;
        this.iePlanReportItemSetService = iePlanReportItemSetService;
        this.iePlanStatisticSetService = iePlanStatisticSetService;
        this.iePlanBusinessHeadSetService = iePlanBusinessHeadSetService;
        this.iePlanBusinessItemSetService = iePlanBusinessItemSetService;
        this.iePlanScreenService = iePlanScreenService;
        resp = new BaseResponse();
        log.info("初始化 IEPlanConfigSetController");
    }

    @ApiOperation(value="获取经营指标分类")
    @RequestMapping("/getIEPlanOperationSet")
    public String getIEPlanOperationSet(){
        List<IEPlanOperationSet> list = iePlanOperationSetService.getAll();
        log.warn("从Odata获取经营指标分类：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="获取业务计算指标")
    @RequestMapping("/getIEPlanCalculationSet")
    public String getIEPlanCalculationSet(){
        List<IEPlanCalculationSet> list = iePlanCalculationSetService.getAll();
        log.warn("从Odata获取业务计算指标：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取维度指标，并存入数据库。")
    @RequestMapping("/getIEPlanDimensionSet")
    public String getIEPlanDimensionSet(){
        List<IEPlanDimensionSet> list = iePlanDimensionSetService.getAll();
        log.warn("从Odata获取维度指标：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="获取业务取数指标")
    @RequestMapping("/getIEPlanSelectDataSet")
    public String getIEPlanSelectDataSet(){
        List<IEPlanSelectDataSet> list = iePlanSelectDataSetService.getAll();
        log.warn("从Odata获取维度指标：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="获取组织机构数据")
    @RequestMapping("/getOrganizationSet")
    public String getOrganizationSet(){
        List<OrganizationSet> list = organizationSetService.getAll();

        log.warn("从Odata获取组织机构数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据dmart获取组织列表（部门、人员）")
    @RequestMapping("/getOrganizationListByDmart")
    public String getOrganizationListByDmart(@RequestBody IEPlanDimensionSet iePlanDimensionSet) throws Exception {
        List<KeyValueBean> list = organizationSetService.getORG(ContextUtils.getCompany().getOrgcode(), iePlanDimensionSet.getDmart(), null);

        log.warn("从Odata获取组织机构数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="获取报表抬头数据")
    @RequestMapping("/getIEPlanReportHeadSet")
    public String getIEPlanReportHeadSet() {
        List<IEPlanReportHeadSet> list = iePlanReportHeadSetService.getAll();
        log.warn("从Odata获取报表抬头数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取报表行项目数据，并存入数据库。")
    @RequestMapping("/getIEPlanReportItemSet")
    public String getIEPlanReportItemSet() {
        List<IEPlanReportItemSet> list = iePlanReportItemSetService.getAll();
        log.warn("从Odata获取报表行项目数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据公司代码获取报表配置信息，参数：1、rptyp-报表类型；2、dmart-公司报表/部门报表；3、rpdat-报表日期（年报yyyy-MM，月报yyyy-MM）。")
    @RequestMapping("/getIEPlanReportHeadVO")
    public String getIEPlanReportHeadVO(HttpSession session, @RequestBody IEPlanReportHeadVO vo) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        vo = iePlanReportHeadSetService.getReportConfigByBukrs(userInfo.getCompanyModel().getOrgcode(), vo.getRptyp(), vo.getDmart(), vo.getRpdat());
        log.warn("从Odata获取取报表配置数据：{}", JSON.toJSONString(vo));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(vo).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据公司代码获取报表配置信息，参数：1、rptyp-报表类型。")
    @RequestMapping("/getIEPlanReportColumnHead")
    public String getIEPlanReportColumnHead(HttpSession session, @RequestBody IEPlanReportHeadVO vo) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        vo = iePlanReportHeadSetService.getReportConfigByBukrs(userInfo.getCompanyModel().getOrgcode(), vo.getRptyp());
        log.warn("从Odata获取取报表配置数据：{}", JSON.toJSONString(vo));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(vo).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据qcgrp，获取数据源列表。")
    @RequestMapping("/getDatasourceSet")
    public String getDatasourceSet(@RequestBody ConfigDataSourceSet configDataSourceSet) {
        String qcgrp = configDataSourceSet.getQcgrp();
        List<ConfigDataSourceSet> list;

        if(Strings.isNullOrEmpty(qcgrp)){
            return resp.setStateCode(BaseResponse.ERROR).setMsg("qcgrp为空！").toJson();
        }
        list = configDataSourceSetService.getListByQcgrp(qcgrp);

        log.warn("获取数据源：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="获取经营指标列表。")
    @RequestMapping("/getIEPlanOperationSetList")
    public String getIEPlanOperationSetList() {
        List<IEPlanOperationSet> list = iePlanOperationSetService.getAll();
        log.warn("获取经营指标列表：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取数据源数据。")
    @RequestMapping("/fetchDatasourceSet")
    public String fetchDatasourceSet() throws Exception {
        List<ConfigDataSourceSet> list = configDataSourceSetService.fetchData();
        log.warn("获取数据源：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="获取统计指标配置数据")
    @RequestMapping("/getIEPlanStatisticSet")
    public String getIEPlanStatisticSet() {
        List<IEPlanStatisticSet> list = iePlanStatisticSetService.getAll();
        log.warn("从Odata获取统计指标配置数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="获取滚动计划-抬头配置数据")
    @RequestMapping("/getIEPlanBusinessHeadSet")
    public String getIEPlanBusinessHeadSet(){
        List<IEPlanBusinessHeadSet> list = iePlanBusinessHeadSetService.getAll();
        log.warn("从Odata获取滚动计划-抬头配置数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="获取滚动计划-计算逻辑明细配置数据")
    @RequestMapping("/getIEPlanBusinessItemSet")
    public String getIEPlanBusinessItemSet() {
        List<IEPlanBusinessItemSet> list = iePlanBusinessItemSetService.getAll();
        log.warn("从Odata获取滚动计划-计算逻辑明细配置数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取屏幕配置数据。")
    @RequestMapping("/getIEPlanScreen")
    public String getIEPlanScreen() throws Exception {
        iePlanScreenService.getAllFromDatasource();
        log.warn("从Odata获取屏幕配置配置数据");
        return resp.setStateCode(BaseResponse.SUCCESS).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据bukrs、rptyp、hview屏幕配置抬头数据列表。")
    @RequestMapping("/getIEPlanScreenHeadSetList")
    public String getIEPlanScreenHeadSetList(@RequestBody IEPlanScreenHeadSet headSet) throws Exception {
        String bukrs = ContextUtils.getCompany().getOrgcode();
        IEPlanScreenVO vo = iePlanScreenService.findHeadByBukrsAndRptypAndHview(bukrs, headSet.getRptyp(), headSet.getHview());
        log.warn("根据bukrs、rptyp、hview屏幕配置抬头数据列表：{}", JSON.toJSONString(vo));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(vo).setMsg("成功！").toJson();
    }


    @ApiOperation(value="刷新所有配置数据。")
    @RequestMapping("/initAll")
    public String initAll() {

        try {
            long l = System.currentTimeMillis();
            iePlanOperationSetService.getAllFromDatasource();
            getIEPlanCalculationSet();
            iePlanCalculationSetService.getAllFromDatasource();
            iePlanSelectDataSetService.getAllFromDatasource();
            iePlanDimensionSetService.getAllFromDatasource();
            organizationSetService.getAllFromDatasource();
            iePlanReportHeadSetService.getAllFromDatasource();
            iePlanReportItemSetService.getAllFromDatasource();
            fetchDatasourceSet();
            iePlanStatisticSetService.getAllFromDatasource();
            iePlanBusinessHeadSetService.getAllFromDatasource();
            iePlanBusinessItemSetService.getAllFromDatasource();
            getIEPlanScreen();
            log.error("配置数据已更新，耗时"+((System.currentTimeMillis()-l)/1000.00)+"秒");
            return resp.setStateCode(BaseResponse.SUCCESS).setMsg("配置数据已更新，耗时"+((System.currentTimeMillis()-l)/1000.00)+"秒").toJson();
        }catch (Exception e){
            log.error("配置数据更新失败："+e.getMessage());
            e.printStackTrace();
            return resp.setStateCode(BaseResponse.ERROR).setMsg("配置数据更新失败："+e.getMessage()).toJson();
        }
    }
}
