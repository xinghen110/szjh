package com.magustek.szjh.configset.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.magustek.szjh.configset.bean.*;
import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.configset.service.*;
import com.magustek.szjh.user.bean.UserInfo;
import com.magustek.szjh.utils.ClassUtils;
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
    private BaseResponse resp;

    public IEPlanConfigSetController(IEPlanOperationSetService iePlanOperationSetService, IEPlanCalculationSetService iePlanCalculationSetService, IEPlanDimensionSetService iePlanDimensionSetService, IEPlanSelectDataSetService iePlanSelectDataSetService, ConfigDataSourceSetService configDataSourceSetService, OrganizationSetService organizationSetService, IEPlanReportHeadSetService iePlanReportHeadSetService, IEPlanReportItemSetService iePlanReportItemSetService) {
        this.iePlanOperationSetService = iePlanOperationSetService;
        this.iePlanCalculationSetService = iePlanCalculationSetService;
        this.iePlanDimensionSetService = iePlanDimensionSetService;
        this.iePlanSelectDataSetService = iePlanSelectDataSetService;
        this.configDataSourceSetService = configDataSourceSetService;
        this.organizationSetService = organizationSetService;
        this.iePlanReportHeadSetService = iePlanReportHeadSetService;
        this.iePlanReportItemSetService = iePlanReportItemSetService;
        resp = new BaseResponse();
        log.info("初始化 IEPlanOperationSetController");
    }

    @ApiOperation(value="从Odata获取经营指标分类，并存入数据库。")
    @RequestMapping("/getIEPlanOperationSet")
    public String getIEPlanOperationSet() throws Exception {
        List<IEPlanOperationSet> list = iePlanOperationSetService.getAllFromDatasource();
        log.warn("从Odata获取经营指标分类：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取业务计算指标，并存入数据库。")
    @RequestMapping("/getIEPlanCalculationSet")
    public String getIEPlanCalculationSet() throws Exception {
        List<IEPlanCalculationSet> list = iePlanCalculationSetService.getAllFromDatasource();
        log.warn("从Odata获取业务计算指标：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取维度指标，并存入数据库。")
    @RequestMapping("/getIEPlanDimensionSet")
    public String getIEPlanDimensionSet() throws Exception {
        List<IEPlanDimensionSet> list = iePlanDimensionSetService.getAllFromDatasource();
        log.warn("从Odata获取维度指标：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取业务取数指标，并存入数据库。")
    @RequestMapping("/getIEPlanSelectDataSet")
    public String getIEPlanSelectDataSet() throws Exception {
        List<IEPlanSelectDataSet> list = iePlanSelectDataSetService.getAllFromDatasource();
        log.warn("从Odata获取维度指标：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取组织机构数据，并存入数据库。")
    @RequestMapping("/getOrganizationSet")
    public String getOrganizationSet() throws Exception {
        List<OrganizationSet> list = organizationSetService.getAllFromDatasource();

        log.warn("从Odata获取组织机构数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取报表抬头数据，并存入数据库。")
    @RequestMapping("/getIEPlanReportHeadSet")
    public String getIEPlanReportHeadSet() throws Exception {
        List<IEPlanReportHeadSet> list = iePlanReportHeadSetService.getAllFromDatasource();
        log.warn("从Odata获取报表抬头数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取报表行项目数据，并存入数据库。")
    @RequestMapping("/getIEPlanReportItemSet")
    public String getIEPlanReportItemSet() throws Exception {
        List<IEPlanReportItemSet> list = iePlanReportItemSetService.getAllFromDatasource();
        log.warn("从Odata获取报表行项目数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据公司代码获取报表配置信息，参数：1、rptyp-报表类型；2、orgdp-公司报表/部门报表；3、rpdat-报表日期（年报yyyy-MM，月报yyyy-MM）。")
    @RequestMapping("/getIEPlanReportHeadVO")
    public String getIEPlanReportHeadVO(HttpSession session, @RequestBody IEPlanReportHeadVO vo) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        vo = iePlanReportHeadSetService.getReportConfigByBukrs(userInfo.getCompanyModel().getOrgcode(), vo.getRptyp(), vo.getOrgdp(), vo.getRpdat());
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
    public String getDatasourceSet(@RequestBody ConfigDataSourceSet configDataSourceSet) throws Exception {
        String qcgrp = configDataSourceSet.getQcgrp();
        List<ConfigDataSourceSet> list;

        if(Strings.isNullOrEmpty(qcgrp)){
            return resp.setStateCode(BaseResponse.ERROR).setMsg("qcgrp为空！").toJson();
        }
        list = configDataSourceSetService.getListByQcgrp(qcgrp);

        log.warn("获取数据源：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取数据源数据。")
    @RequestMapping("/fetchDatasourceSet")
    public String fetchDatasourceSet() throws Exception {
        List<ConfigDataSourceSet> list = configDataSourceSetService.fetchData();
        log.warn("获取数据源：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }
}
