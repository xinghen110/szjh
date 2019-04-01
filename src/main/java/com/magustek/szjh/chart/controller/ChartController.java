package com.magustek.szjh.chart.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.chart.bean.vo.DmCalcVO;
import com.magustek.szjh.chart.service.ChartService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 图表接口
 * */
@Api("图表接口")
@Slf4j
@RestController
@RequestMapping(value = "/chart", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class ChartController {
    private BaseResponse resp;
    private ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
        resp = new BaseResponse();
        log.info("初始化 ChartController");
    }

    @ApiOperation(value="根据日期范围（起始、截止日期）、组织机构代码（多个）、业务计算指标，返回业务计算指标值")
    @RequestMapping("/dmCalc")
    public String dmCalc(@RequestBody DmCalcVO vo){
        Map<String, List<Map<String, String>>> list = chartService.dmCalc(vo);
        log.warn("根据日期范围（起始、截止日期）、组织机构代码（多个）、业务计算指标，返回业务计算指标值：{}", JSON.toJSONString(list));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(list).setMsg("成功！").toJson();
    }
}
