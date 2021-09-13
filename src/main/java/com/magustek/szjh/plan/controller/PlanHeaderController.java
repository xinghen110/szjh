package com.magustek.szjh.plan.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.configset.bean.vo.IEPlanReportHeadVO;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.vo.PlanHeaderVO;
import com.magustek.szjh.plan.bean.vo.RollPlanHeadDataArchiveVO;
import com.magustek.szjh.plan.service.PlanHeaderService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import com.magustek.szjh.utils.constant.ModelCons;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 计划抬头
 * */
@Api("计划抬头")
@Slf4j
@RestController
@RequestMapping(value = "/planHeader", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class PlanHeaderController {
    private BaseResponse resp;
    private PlanHeaderService planHeaderService;

    public PlanHeaderController(PlanHeaderService planHeaderService) {
        this.planHeaderService = planHeaderService;
        resp = new BaseResponse();
    }

    @ApiOperation(value="保存计划抬头（保存抬头后，会自动生成报表布局并初始化数据）", notes = "参数：参考PlanHeader结构")
    @RequestMapping("/save")
    public String savePlanHeader(@RequestBody PlanHeader header) throws Exception {
        long l = System.currentTimeMillis();
        header = planHeaderService.save(header);
        String userName = ContextUtils.getUserName();
        log.warn("{}保存计划抬头：{}", userName, JSON.toJSONString(header));
        log.warn("保存计划耗时：{}秒", (System.currentTimeMillis()-l)/1000.00);
        return resp.setStateCode(BaseResponse.SUCCESS).setData(header).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据id删除计划", notes = "参数：id")
    @RequestMapping("/deletePlanHeader")
    public String deletePlanHeader(@RequestBody PlanHeader header) {
        header = planHeaderService.delete(header);
        String userName = ContextUtils.getUserName();
        log.warn("{}删除计划：{}", userName, JSON.toJSONString(header));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(header).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据id获取计划", notes = "参数：id")
    @RequestMapping("/getPlanHeaderById")
    public String getPlanHeaderById(@RequestBody PlanHeader header) {
        header = planHeaderService.getById(header.getId());
        String userName = ContextUtils.getUserName();
        log.warn("{}根据id获取计划：{}", userName, JSON.toJSONString(header));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(header).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据年-月，获取该月最新的计划", notes = "参数：yyyy-MM")
    @RequestMapping("/getLastPlanByMonth")
    public String getLastPlanByMonth(@RequestBody PlanHeaderVO vo) {
        PlanHeader header = planHeaderService.getLastPlanByMonth(vo.getDtval());
        String userName = ContextUtils.getUserName();
        log.warn("{}根据月份获取最新的计划：{}", userName, JSON.toJSONString(header));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(header).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据公司获取计划列表", notes = "参数：rporg、rptyp、powerModel")
    @RequestMapping("/getPlanHeaderByBukrs")
    public String getPlanHeaderByBukrs(@RequestBody PlanHeaderVO vo) throws Exception {
        if(vo.getPowerModel().equals(ModelCons.APPROVAL)){
            vo.setStonr("20");
        }
        Page<Map<String, String>> listByBukrs = planHeaderService.getListByBukrs(vo, vo.initPageRequest());
        String userName = ContextUtils.getUserName();
        log.warn("{}根据公司获取计划列表：{}", userName, JSON.toJSONString(listByBukrs));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(listByBukrs).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据计划抬头ID代码获取报表布局信息，参数：1、id。")
    @RequestMapping("/getLayoutByHeaderId")
    public String getLayoutByHeaderId(@RequestBody PlanHeaderVO vo) {
        IEPlanReportHeadVO config = planHeaderService.getLayoutByHeaderId(vo.getId());
        log.warn("根据计划抬头ID代码获取报表布局信息：{}", JSON.toJSONString(config));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(config).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据参数获取合同信息列表，参数：zbart-指标值、dmval-维度值、dtval-时间（yyyy-MM）、id-月度计划id。")
    @RequestMapping("/getHtsnoList")
    public String getHtsnoList(@RequestBody PlanHeaderVO vo) throws Exception {
        List<Map<String, String>> htsnoList = planHeaderService.getHtsnoList(vo.getZbart(), vo.getDmval(), vo.getDtval(), vo.getId(), vo.initPageRequest());
        log.warn("根据参数获取合同信息列表：{}", JSON.toJSONString(htsnoList));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(htsnoList).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据参数获取合同信息分页列表，参数：zbart-指标值、dmval-维度值、dtval-时间（yyyy-MM）、id-月度计划id、searching-查询字段。")
    @RequestMapping("/getHtsnoListPage")
    public String getHtsnoListPage(@RequestBody PlanHeaderVO vo) {
        Page<Map<String, String>> page = null;
        try {
            page = planHeaderService.getHtsnoListPage(vo.getZbart(), vo.getDmval(), vo.getDtval(), vo.getId(), vo.initPageRequest(), vo.getSearching(), vo.getHview(), vo.getRptyp());
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        log.warn("根据参数获取合同信息列表：{}", JSON.toJSONString(page));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(page).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据计划ID-planHeadId、业务计算指标-caart、维度-dmart、经营指标分类-zbart，获取账期列表。")
    @RequestMapping("/getCavalByPlanHeadIdAndCaartAndDmval")
    public String getCavalByPlanHeadIdAndCaartAndDmval(@RequestBody RollPlanHeadDataArchiveVO vo) {
        List<Map<String, Object>> cavalList = planHeaderService.getCavalByPlanHeadIdAndCaartAndDmart(vo.getPlanHeadId(), vo.getCaart(), vo.getDmart(), vo.getZbart());
        log.warn("根据计划ID-planHeadId、业务计算指标-caart、维度-dmart、经营指标分类-zbart，获取账期列表：{}", JSON.toJSONString(cavalList));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(cavalList).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据计划ID-planHeadId、业务计算指标-caart、维度-dmart、经营指标分类-zbart、组织机构代码-dmval、历史能力值-caval，更新账期列表。")
    @RequestMapping("/updateCavalByPlanHeadIdAndCaartAndDmartAndDmval")
    public String updateCavalByPlanHeadIdAndCaartAndDmartAndDmval(@RequestBody List<RollPlanHeadDataArchiveVO> voList) throws Exception {
        //Assert.isTrue(!ClassUtils.isEmpty(voList),"参数不正确！");
        int count = 0;
        for(RollPlanHeadDataArchiveVO vo : voList){
            if(vo.getCaval() != 0){
                count += planHeaderService.updateCavalByPlanHeadIdAndCaartAndDmartAndDmval(
                    vo.getPlanHeadId(),
                    vo.getCaart(),
                    vo.getDmart(),
                    vo.getDmval(),
                    vo.getZbart(),
                    vo.getCaval());
            }
        }
        log.warn("根据计划ID-planHeadId、业务计算指标-caart、维度-dmart、经营指标分类-zbart，更新账期列表：{}", count);
        return resp.setStateCode(BaseResponse.SUCCESS).setData(count).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据计划ID-planHeadId、经营指标分类-zbart，获取计划金额-wears、计划笔数-count、最大可调整金额-totalWears。")
    @RequestMapping("/getCavalByPlanHeadId")
    public String getCavalByPlanHeadId(@RequestBody RollPlanHeadDataArchiveVO vo) throws Exception {
        Map<String, String> map = planHeaderService.getCavalByPlanHeadId(vo.getPlanHeadId(), vo.getZbart());
        log.warn("用户-{}，根据计划ID-planHeadId-{}、经营指标分类-zbart-{}、目标金额-wears-{}。",
                ContextUtils.getUserName(),
                vo.getPlanHeadId(),
                vo.getZbart(),
                vo.getWears());
        return resp.setStateCode(BaseResponse.SUCCESS).setData(map).setMsg("成功！").toJson();
    }

    @ApiOperation(value="根据计划ID-planHeadId、经营指标分类-zbart、目标金额-wears，更新月度计划。")
    @RequestMapping("/updateCavalByPlanHeadIdAndWears")
    public String updateCavalByPlanHeadIdAndWears(@RequestBody RollPlanHeadDataArchiveVO vo) throws Exception {
        planHeaderService.updateCavalByPlanHeadIdAndZbartAndWears(vo.getPlanHeadId(), vo.getZbart(), vo.getWears(), vo.getDmart(), vo.getDmval());
        log.warn("用户-{}，根据计划ID-planHeadId-{}、经营指标分类-zbart-{}、目标金额-wears-{}，更新月度计划。",
                ContextUtils.getUserName(),
                vo.getPlanHeadId(),
                vo.getZbart(),
                vo.getWears());
        return resp.setStateCode(BaseResponse.SUCCESS).setMsg("成功！").toJson();
    }

    @ApiOperation(value="获取审批界面", notes = "参数：id")
    @RequestMapping("/getApprovalPage")
    public String getApprovalPage(@RequestBody PlanHeaderVO vo) throws Exception{
        PlanHeader planHeader = planHeaderService.getApprovalPage(vo);
        return resp.setStateCode(BaseResponse.SUCCESS).setData(planHeader).setMsg("成功！").toJson();
    }

    @ApiOperation(value="审批模块:提交审批、同意、驳回", notes = "参数：id、apinfo、approvalMode")
    @RequestMapping("/approvalProcess")
    public String approvalProcess(@RequestBody PlanHeaderVO vo) throws Exception{
        PlanHeader planHeader = planHeaderService.approvalProcess(vo);
        return resp.setStateCode(BaseResponse.SUCCESS).setData(planHeader).setMsg("成功！").toJson();
    }

    @ApiOperation(value="月度计划下达", notes = "参数：id")
    @RequestMapping("/issuePlan")
    public String issuePlan(@RequestBody PlanHeaderVO vo) {
        boolean success = planHeaderService.issuePlan(vo.getId());
        if(success){
            return resp.setStateCode(BaseResponse.SUCCESS).setMsg("下达成功！").toJson();
        }else{
            return resp.setStateCode(BaseResponse.ERROR).setMsg("下达失败！").toJson();
        }
    }
}
