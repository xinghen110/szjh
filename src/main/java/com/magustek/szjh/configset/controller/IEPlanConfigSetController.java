package com.magustek.szjh.configset.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.configset.bean.IEPlanCalculationSet;
import com.magustek.szjh.configset.bean.IEPlanDimensionSet;
import com.magustek.szjh.configset.bean.IEPlanOperationSet;
import com.magustek.szjh.configset.bean.IEPlanSelectDataSet;
import com.magustek.szjh.configset.service.IEPlanCalculationSetService;
import com.magustek.szjh.configset.service.IEPlanDimensionSetService;
import com.magustek.szjh.configset.service.IEPlanOperationSetService;
import com.magustek.szjh.configset.service.IEPlanSelectDataSetService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 配置表：经营指标分类
 * */
@Api("配置表：经营指标分类")
@Slf4j
@RestController
@RequestMapping(value = "/IEPlanConfigSet", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class IEPlanConfigSetController {

    private IEPlanOperationSetService iePlanOperationSetService;
    private IEPlanCalculationSetService iePlanCalculationSetService;
    private IEPlanDimensionSetService iePlanDimensionSetService;
    private IEPlanSelectDataSetService iePlanSelectDataSetService;
    private BaseResponse resp;

    public IEPlanConfigSetController(IEPlanOperationSetService iePlanOperationSetService, IEPlanCalculationSetService iePlanCalculationSetService, IEPlanDimensionSetService iePlanDimensionSetService, IEPlanSelectDataSetService iePlanSelectDataSetService) {
        this.iePlanOperationSetService = iePlanOperationSetService;
        this.iePlanCalculationSetService = iePlanCalculationSetService;
        this.iePlanDimensionSetService = iePlanDimensionSetService;
        this.iePlanSelectDataSetService = iePlanSelectDataSetService;
        resp = new BaseResponse();
        log.info("初始化 IEPlanOperationSetController");
    }

    @ApiOperation(value="从Odata获取经营指标分类，并存入数据库。", notes="从Odata获取经营指标分类，并存入数据库。")
    @RequestMapping("/getIEPlanOperationSet")
    public String getIEPlanOperationSet() throws Exception {
        List<IEPlanOperationSet> list = iePlanOperationSetService.getAllFromDatasource();
        log.warn("从Odata获取经营指标分类：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取业务计算指标，并存入数据库。", notes="从Odata获取业务计算指标，并存入数据库。")
    @RequestMapping("/getIEPlanCalculationSet")
    public String getIEPlanCalculationSet() throws Exception {
        List<IEPlanCalculationSet> list = iePlanCalculationSetService.getAllFromDatasource();
        log.warn("从Odata获取业务计算指标：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取维度指标，并存入数据库。", notes="从Odata获取维度指标，并存入数据库。")
    @RequestMapping("/getIEPlanDimensionSet")
    public String getIEPlanDimensionSet() throws Exception {
        List<IEPlanDimensionSet> list = iePlanDimensionSetService.getAllFromDatasource();
        log.warn("从Odata获取维度指标：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取业务取数指标，并存入数据库。", notes="从Odata获取业务取数指标，并存入数据库。")
    @RequestMapping("/getIEPlanSelectDataSet")
    public String getIEPlanSelectDataSet() throws Exception {
        List<IEPlanSelectDataSet> list = iePlanSelectDataSetService.getAllFromDatasource();
        log.warn("从Odata获取维度指标：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }
}
