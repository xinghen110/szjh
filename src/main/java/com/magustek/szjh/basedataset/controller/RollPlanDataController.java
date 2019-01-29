package com.magustek.szjh.basedataset.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.magustek.szjh.basedataset.entity.vo.RollPlanHeaderVO;
import com.magustek.szjh.basedataset.service.RollPlanDataService;
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
import java.util.Map;

/**
 * 滚动计划
 * */
@Api("滚动计划")
@Slf4j
@RestController
@RequestMapping(value = "/RollPlanData", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class RollPlanDataController {
    private RollPlanDataService rollPlanDataService;
    private BaseResponse resp;

    public RollPlanDataController(RollPlanDataService rollPlanDataService) {
        this.rollPlanDataService = rollPlanDataService;
        this.resp = new BaseResponse();
    }


    @ApiOperation(value="根据version和htsno获取合同滚动计划数据。")
    @RequestMapping("/getRollPlanDataByVersionAndHtsno")
    public String getRollPlanDataByVersionAndHtsno(@RequestBody RollPlanHeaderVO vo) {
        if(Strings.isNullOrEmpty(vo.getVersion())){
            vo.setVersion(LocalDate.now().toString());
        }

        if(Strings.isNullOrEmpty(vo.getHtsno())){
            return resp.setStateCode(BaseResponse.ERROR).setMsg("合同流水号为空").toJson();
        }

        List<RollPlanHeaderVO> list = rollPlanDataService.getRollPlanVOByIdAndHtsno(vo.getId(), vo.getHtsno());

        List<Map<String, String>> maps = rollPlanDataService.coverToMap(list);
        log.warn("version和htsno，合同滚动计划数据：{}", JSON.toJSONString(maps));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(maps).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据version和htsno获取合同数据。")
    @RequestMapping("/getContractDataByVersionAndHtsno")
    public String getContractDataByVersionAndHtsno(@RequestBody RollPlanHeaderVO vo) throws Exception {

        checkVersion(vo);

        if(Strings.isNullOrEmpty(vo.getHtsno())){
            return resp.setStateCode(BaseResponse.ERROR).setMsg("合同流水号为空").toJson();
        }

        Map<String, String> contract = rollPlanDataService.getContractDataByVersionAndHtsno(vo.getVersion(), vo.getHtsno());

        log.warn("合同数据：{}", JSON.toJSONString(contract));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(contract).setMsg("成功！").toJson();
    }

    private void checkVersion(RollPlanHeaderVO vo){
        if(Strings.isNullOrEmpty(vo.getVersion())){
            vo.setVersion(LocalDate.now().toString());
        }
    }
}
