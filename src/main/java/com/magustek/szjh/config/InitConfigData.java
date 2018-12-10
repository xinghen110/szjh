package com.magustek.szjh.config;

import com.magustek.szjh.configset.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("InitConfigData")
public class InitConfigData {
    private IEPlanOperationSetService iePlanOperationSetService;
    private IEPlanCalculationSetService iePlanCalculationSetService;
    private IEPlanDimensionSetService iePlanDimensionSetService;
    private IEPlanSelectDataSetService iePlanSelectDataSetService;

    private OrganizationSetService organizationSetService;
    private IEPlanReportHeadSetService iePlanReportHeadSetService;
    private IEPlanReportItemSetService iePlanReportItemSetService;

    private ConfigDataSourceSetService configDataSourceSetService;

    public InitConfigData(IEPlanOperationSetService iePlanOperationSetService, IEPlanCalculationSetService iePlanCalculationSetService, IEPlanDimensionSetService iePlanDimensionSetService, IEPlanSelectDataSetService iePlanSelectDataSetService, OrganizationSetService organizationSetService, IEPlanReportHeadSetService iePlanReportHeadSetService, IEPlanReportItemSetService iePlanReportItemSetService, ConfigDataSourceSetService configDataSourceSetService) {
        this.iePlanOperationSetService = iePlanOperationSetService;
        this.iePlanCalculationSetService = iePlanCalculationSetService;
        this.iePlanDimensionSetService = iePlanDimensionSetService;
        this.iePlanSelectDataSetService = iePlanSelectDataSetService;
        this.organizationSetService = organizationSetService;
        this.iePlanReportHeadSetService = iePlanReportHeadSetService;
        this.iePlanReportItemSetService = iePlanReportItemSetService;
        this.configDataSourceSetService = configDataSourceSetService;
    }

    public void init() throws Exception{
        try {
            long start = System.currentTimeMillis();
            log.warn("配置信息初始化开始！");
            iePlanOperationSetService.getAllFromDatasource();       //从Odata获取经营指标分类，并存入数据库。
            iePlanCalculationSetService.getAllFromDatasource();     //从Odata获取业务计算指标，并存入数据库。
            iePlanDimensionSetService.getAllFromDatasource();       //从Odata获取维度指标，并存入数据库。
            iePlanSelectDataSetService.getAllFromDatasource();      //从Odata获取业务取数指标，并存入数据库。
            organizationSetService.getAllFromDatasource();          //从Odata获取组织机构数据，并存入数据库。
            iePlanReportHeadSetService.getAllFromDatasource();      //从Odata获取报表抬头数据，并存入数据库。
            iePlanReportItemSetService.getAllFromDatasource();      //从Odata获取报表行项目数据，并存入数据库。
            configDataSourceSetService.fetchData();                 //从Odata获取数据源数据，并存入数据库。
            log.warn("配置信息初始化完成！耗时："+((System.currentTimeMillis()-start)/1000.00)+"秒");
        } catch (Exception e) {
            log.error("配置信息初始化出错！"+e.getMessage());
            throw e;
        }
    }
}
