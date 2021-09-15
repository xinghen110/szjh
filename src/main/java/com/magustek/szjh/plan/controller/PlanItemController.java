package com.magustek.szjh.plan.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.PlanItem;
import com.magustek.szjh.plan.bean.vo.PlanItemVO;
import com.magustek.szjh.plan.service.PlanHeaderService;
import com.magustek.szjh.plan.service.PlanItemService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计划明细
 * */
@Api("计划明细")
@Slf4j
@RestController
@RequestMapping(value = "/planItem", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class PlanItemController {
    private final BaseResponse resp;
    private final PlanItemService planItemService;
    private final PlanHeaderService planHeaderService;

    public PlanItemController(PlanItemService planItemService, PlanHeaderService planHeaderService) {
        this.planItemService = planItemService;
        this.planHeaderService = planHeaderService;
        resp = new BaseResponse();
    }

    @ApiOperation(value="更新计划明细指标值", notes = "参数：id、zbval")
    @RequestMapping("/updateById")
    public String updateById(@RequestBody PlanItem[] items) throws Exception {
        items = planItemService.save(items);
        String userName = ContextUtils.getUserName();
        log.warn("{}更新计划明细指标值：{}", userName, JSON.toJSONString(items));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(items).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据headerId获取计划明细", notes = "参数：headerId、zaxis、zvalue")
    @RequestMapping("/getItemByHeaderId")
    public String getItemByHeaderId(@RequestBody PlanItemVO vo) throws Exception {
        List<PlanItem> list = planItemService.getListByHeaderId(vo.getHeaderId(), vo.getZaxis(), vo.getZvalue());
        List<Map<String, String>> maps = planItemService.coverListToMap(list);
        String userName = ContextUtils.getUserName();
        log.warn("{}根据headerId获取计划明细：{}", userName, JSON.toJSONString(maps));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(maps).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据headerId获取计划明细（计划月份的值）", notes = "参数：headerId、zaxis、zvalue")
    @RequestMapping("/getJHItemByHeaderId")
    public String getJHItemByHeaderId(@RequestBody PlanItemVO vo) throws Exception {
        PlanHeader header = planHeaderService.getById(vo.getHeaderId());
        String jhval = header.getJhval();
        List<PlanItem> list = planItemService.getListByHeaderId(vo.getHeaderId(), vo.getZaxis(), vo.getZvalue());
        List<Map<String, String>> maps = planItemService.coverListToMap(list);
        List<Map<String, String>> result = new ArrayList<>(maps.size());
        maps.forEach(item->{
            Map<String, String> itemMap = new HashMap<>();
            result.add(itemMap);
            item.forEach((k,v)->{
                if(k.contains(jhval)){
                    itemMap.put(k.replace(jhval+" ", "wears"), v);
                }
                if(k.contains("y_")){
                    itemMap.put(k, v);
                }
            });
            itemMap.put("target", itemMap.get("wears"));
        });
        String userName = ContextUtils.getUserName();
        log.warn("{}根据headerId获取计划明细：{}", userName, JSON.toJSONString(result));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(result).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据计划抬头ID代码获取报表布局信息（对比），参数：1、id。")
    @RequestMapping("/getCompareLayoutByHeaderId")
    public String getCompareLayoutByHeaderId(@RequestBody PlanItemVO vo) {
        IEPlanReportHeadVO config = planItemService.getCompareLayoutByHeaderId(vo.getHeaderId());
        log.warn("根据计划抬头ID代码获取报表布局信息：{}", JSON.toJSONString(config));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(config).setMsg("成功！").toJson();
    }
    @ApiOperation(value="根据headerId获取计划明细调整前后对比数据", notes = "参数：headerId、zaxis、zvalue")
    @RequestMapping("/getCompareItemByHeaderId")
    public String getCompareItemByHeaderId(@RequestBody PlanItemVO vo) throws Exception {
        List<PlanItem> list = planItemService.getComparedListByHeaderId(vo);
        List<Map<String, String>> maps = planItemService.coverListToMap(list);
        log.warn("根据headerId获取计划明细：{}", JSON.toJSONString(maps));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(maps).setMsg("成功！").toJson();
    }
}
