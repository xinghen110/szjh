package com.magustek.szjh.basedataset.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.magustek.szjh.basedataset.entity.*;
import com.magustek.szjh.basedataset.entity.vo.IEPlanDimenValueSetVO;
import com.magustek.szjh.basedataset.entity.vo.IEPlanSelectValueSetVO;
import com.magustek.szjh.basedataset.service.*;
import com.magustek.szjh.configset.controller.IEPlanConfigSetController;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * 计划业务表：历史业务取数主表
 * */
@Component
@Api("计划业务表")
@Slf4j
@RestController
@RequestMapping(value = "/IEPlanBaseDataSet", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class BasedataSetController {
    private IEPlanSelectValueSetService iePlanSelectValueSetService;
    private IEPlanDimenValueSetService iePlanDimenValueSetService;
    private CalculateResultService calculateResultService;
    private DmCalcStatisticsService dmCalcStatisticsService;
    private IEPlanPaymentSetService iePlanPaymentSetService;
    private IEPlanTermsSetService iePlanTermsSetService;
    private IEPlanConfigSetController iePlanConfigSetController;
    private RollPlanDataService rollPlanDataService;

    private BaseResponse resp;

    public BasedataSetController(IEPlanSelectValueSetService iePlanContractHeadSetService, IEPlanDimenValueSetService iePlanDimenValueSetService, CalculateResultService calculateResultService, DmCalcStatisticsService dmCalcStatisticsService, IEPlanPaymentSetService iePlanPaymentSetService, IEPlanTermsSetService iePlanTermsSetService, IEPlanConfigSetController iePlanConfigSetController, RollPlanDataService rollPlanDataService) {
        this.iePlanSelectValueSetService = iePlanContractHeadSetService;
        this.iePlanDimenValueSetService = iePlanDimenValueSetService;
        this.calculateResultService = calculateResultService;
        this.dmCalcStatisticsService = dmCalcStatisticsService;
        this.iePlanPaymentSetService = iePlanPaymentSetService;
        this.iePlanTermsSetService = iePlanTermsSetService;
        this.iePlanConfigSetController = iePlanConfigSetController;
        this.rollPlanDataService = rollPlanDataService;
        resp = new BaseResponse();
    }

    @ApiOperation(value="从Odata获取历史业务取数明细，并存入数据库。")
    @RequestMapping("/getIEPlanSelectValueSet")
    public String getIEPlanSelectValueSet() throws Exception {
        List<IEPlanSelectValueSet> list = iePlanSelectValueSetService.fetchData();
        log.warn("从Odata获取历史业务取数明细：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list.size()).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据htsno、version获取合同指标数据。参数1、htsno，2、version（如果不指定，默认是当天）。")
    @RequestMapping("/getContractSdByHtsno")
    public String getContractSdByHtsno(@RequestBody IEPlanSelectValueSetVO vo) throws Exception {
        List<IEPlanSelectValueSetVO> list = iePlanSelectValueSetService.getContractByHtsnoAndVersionGroupByHtnum(vo.getHtsno(), vo.getVersion());
        log.warn("合同指标数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }


    @ApiOperation(value="从Odata获取历史业务维度明细，并存入数据库。")
    @RequestMapping("/getIEPlanDimenValueSet")
    public String getIEPlanDimenValueSet() throws Exception {
        List<IEPlanDimenValueSet> list = iePlanDimenValueSetService.fetchData();
        //log.warn("从Odata获取历史业务维度明细：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list.size()).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据htsno、version获取合同维度数据。参数1、htsno，2、version（如果不指定，默认是当天）。")
    @RequestMapping("/getContractDmByHtsno")
    public String getContractDmByHtsno(@RequestBody IEPlanDimenValueSetVO vo) throws Exception {
        List<IEPlanDimenValueSetVO> list = iePlanDimenValueSetService.getContractByHtsno(vo.getHtsno(), vo.getVersion());
        log.warn("合同维度数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据取数明细及业务计算指标，进行计算并保存结果。")
    @RequestMapping("/calculate")
    public String calculate(@RequestBody DmCalcStatistics result) {
        String version = version(result);
        List<CalculateResult> list = calculateResultService.calculateByVersion(version);
        log.warn("计算版本为【{}】的取数明细结果为：{}", version, JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list.size()).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据维度明细及计算指标进行计算，并保存结果。")
    @RequestMapping("/statisticByVersion")
    public String statisticByVersion(@RequestBody DmCalcStatistics result) {
        String version = version(result);
        int size = dmCalcStatisticsService.statisticByVersion(version);
        log.warn("计算版本为【{}】的取数明细结果为：{}", version, JSON.toJSONString(size));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(size).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取合同付款构成明细，并存入数据库。")
    @RequestMapping("/getIEPlanPaymentSet")
    public String getIEPlanPaymentSet() throws Exception {
        List<IEPlanPaymentSet> list = iePlanPaymentSetService.fetchData();
        log.warn("从Odata获取合同付款构成明细：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list.size()).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取合同合同收付款条款明细，并存入数据库。")
    @RequestMapping("/getIEPlanTermsSet")
    public String getIEPlanTermsSet() throws Exception {
        List<IEPlanTermsSet> list = iePlanTermsSetService.fetchData();
        log.warn("从Odata获取合同合同收付款条款明细：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list.size()).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据版本号，计算滚动计划数据。")
    @RequestMapping("/calcRollPlanData")
    public String calcRollPlanData(@RequestBody DmCalcStatistics result) throws Exception{
        String version = version(result);

        List<RollPlanHeadData> list = rollPlanDataService.calculateByVersion(version);
        log.warn("根据版本号，计算滚动计划数据：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list.size()).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取所有配置及业务数据，并存入数据库。")
    @RequestMapping("/fetchAllBaseData")
    public void fetchBaseData(@RequestBody DmCalcStatistics result) throws Exception {
        long l = System.currentTimeMillis();
        iePlanConfigSetController.initAll();//获取所有配置数据
        log.warn("获取所有配置数据耗时{}", (System.currentTimeMillis()-l)/1000.00);
        getIEPlanSelectValueSet();
        getIEPlanDimenValueSet();
        //getIEPlanPaymentSet();
        //getIEPlanTermsSet();
        executeAllCalc(result);
        log.warn("从Odata获取所有配置及业务数据耗时{}", (System.currentTimeMillis()-l)/1000.00);
    }

    @ApiOperation(value="执行所有计算。")
    @RequestMapping("/executeAllCalc")
    private void executeAllCalc(DmCalcStatistics result) throws Exception{
        calculate(result);//计算合同指标
        statisticByVersion(result);//将合同指标的计算结果，根据维度进行统计，形成历史能力值
        calcRollPlanData(result);//根据历史能力值，计算滚动计划
    }

    private String version(DmCalcStatistics result){
        if(result == null || Strings.isNullOrEmpty(result.getVersion())){
            return LocalDate.now().toString();
        }else{
            return result.getVersion();
        }
    }
}
