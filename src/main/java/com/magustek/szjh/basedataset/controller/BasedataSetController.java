package com.magustek.szjh.basedataset.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.basedataset.entity.IEPlanDimenValueSet;
import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.basedataset.service.IEPlanDimenValueSetService;
import com.magustek.szjh.basedataset.service.IEPlanSelectValueSetService;
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
 * 计划业务表：历史业务取数主表
 * */
@Api("计划业务表")
@Slf4j
@RestController
@RequestMapping(value = "/IEPlanBaseDataSet", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class BasedataSetController {
    private IEPlanSelectValueSetService iePlanSelectValueSetService;
    private IEPlanDimenValueSetService iePlanDimenValueSetService;
    private BaseResponse resp;

    public BasedataSetController(IEPlanSelectValueSetService iePlanContractHeadSetService, IEPlanDimenValueSetService iePlanDimenValueSetService) {
        this.iePlanSelectValueSetService = iePlanContractHeadSetService;
        this.iePlanDimenValueSetService = iePlanDimenValueSetService;
        resp = new BaseResponse();
    }

    @ApiOperation(value="从Odata获取历史业务取数明细，并存入数据库。")
    @RequestMapping("/getIEPlanSelectValueSet")
    public String getIEPlanSelectValueSet() throws Exception {
        List<IEPlanSelectValueSet> list = iePlanSelectValueSetService.fetchData();
        log.warn("从Odata获取历史业务取数明细：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list.size()).setMsg("成功！").toJson();
    }

    @ApiOperation(value="从Odata获取历史业务维度明细，并存入数据库。")
    @RequestMapping("/getIEPlanDimenValueSet")
    public String getIEPlanDimenValueSet() throws Exception {
        List<IEPlanDimenValueSet> list = iePlanDimenValueSetService.fetchData();
        //log.warn("从Odata获取历史业务维度明细：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list.size()).setMsg("成功！").toJson();
    }
}
