package com.magustek.szjh.basedataset.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.basedataset.entity.CalculateResult;
import com.magustek.szjh.basedataset.entity.DmCalcStatistics;
import com.magustek.szjh.basedataset.entity.IEPlanDimenValueSet;
import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.basedataset.entity.vo.IEPlanDimenValueSetVO;
import com.magustek.szjh.basedataset.entity.vo.IEPlanSelectValueSetVO;
import com.magustek.szjh.basedataset.service.CalculateResultService;
import com.magustek.szjh.basedataset.service.DmCalcStatisticsService;
import com.magustek.szjh.basedataset.service.IEPlanDimenValueSetService;
import com.magustek.szjh.basedataset.service.IEPlanSelectValueSetService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * 计划业务表：历史业务取数主表
 * */
@Api("计划业务表")
@Slf4j
@RestController
@RequestMapping(value = "/IEPlanBaseDataSet", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class BasedataSetController {
    private IEPlanSelectValueSetService iePlanSelectValueSetService;
    private IEPlanDimenValueSetService iePlanDimenValueSetService;
    private CalculateResultService calculateResultService;
    private DmCalcStatisticsService dmCalcStatisticsService;
    private BaseResponse resp;

    public BasedataSetController(IEPlanSelectValueSetService iePlanContractHeadSetService, IEPlanDimenValueSetService iePlanDimenValueSetService, CalculateResultService calculateResultService, DmCalcStatisticsService dmCalcStatisticsService) {
        this.iePlanSelectValueSetService = iePlanContractHeadSetService;
        this.iePlanDimenValueSetService = iePlanDimenValueSetService;
        this.calculateResultService = calculateResultService;
        this.dmCalcStatisticsService = dmCalcStatisticsService;
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
        List<IEPlanSelectValueSetVO> list = iePlanSelectValueSetService.getContractByHtsno(vo.getHtsno(), vo.getVersion());
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
    public String calculate(@RequestBody CalculateResult result) {
        String version;
        if(result == null){
            version = LocalDate.now().toString();
        }else{
            version = result.getVersion();
        }
        List<CalculateResult> list = calculateResultService.calculateByVersion(version);
        log.warn("计算版本为【{}】的取数明细结果为：{}", version, JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list.size()).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据维度明细及计算指标进行计算，并保存结果。")
    @RequestMapping("/statisticByVersion")
    public String statisticByVersion(@RequestBody DmCalcStatistics result) {
        String version;
        if(result == null){
            version = LocalDate.now().toString();
        }else{
            version = result.getVersion();
        }
        int size = dmCalcStatisticsService.statisticByVersion(version);
        log.warn("计算版本为【{}】的取数明细结果为：{}", version, JSON.toJSONString(size));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(size).setMsg("成功！").toJson();
    }
}
