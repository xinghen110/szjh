package com.magustek.szjh.Holiday.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.Holiday.bean.Holiday;
import com.magustek.szjh.Holiday.service.HolidayService;
import com.magustek.szjh.user.bean.UserInfo;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

/**
 * 合同管理:节假日维护
 * */
@Api("合同管理:节假日维护")
@Slf4j
@RestController
@RequestMapping(value = "/holiday", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class HolidayController {
    private HolidayService holidayService;
    private BaseResponse resp;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
        resp = new BaseResponse();
        log.info("初始化 HolidayController");
    }

    @ApiOperation(value="保存假日数据（只能修改日期类型-假日、工作日）", notes="保存假日数据（只能修改日期类型-假日、工作日）")
    @RequestMapping("/save")
    public String save(@RequestBody Holiday holiday, HttpSession httpSession){
        try{
            UserInfo user = (UserInfo)httpSession.getAttribute("userInfo");
            holiday = holidayService.save(holiday);
            log.warn("{}--{}保存节假日信息：{}", ClassUtils.now(), user.getLoginname(), holiday.toJson());
            return resp.setStateCode(BaseResponse.SUCCESS).setData(holiday).setMsg("保存成功！").toJson();
        }catch (Exception e){
            return resp.setStateCode(BaseResponse.ERROR).setMsg(e.getMessage()).toJson();
        }
    }

    @ApiOperation(value="创建一年的数据", notes="创建一年的数据")
    @RequestMapping("/add")
    public String add(@RequestBody Holiday holiday, HttpSession httpSession){
        try{
            UserInfo user = (UserInfo)httpSession.getAttribute("userInfo");
            Iterable<Holiday> holidays = holidayService.addYear(holiday.getYear());
            log.warn("{}--{}创建节假日信息：{}", ClassUtils.now(), user.getLoginname(), JSON.toJSONString(holidays));
            return resp.setStateCode(BaseResponse.SUCCESS).setMsg("创建成功！").toJson();
        }catch (Exception e){
            return resp.setStateCode(BaseResponse.ERROR).setMsg(e.getMessage()).toJson();
        }
    }

    @ApiOperation(value="根据ID获取节假日", notes="根据ID获取节假日")
    @RequestMapping("/get")
    public String get(@RequestBody Holiday holiday){
        try{
            holiday = holidayService.getById(holiday);
            return resp.setStateCode(BaseResponse.SUCCESS).setData(holiday).toJson();
        }catch (Exception e){
            return resp.setStateCode(BaseResponse.ERROR).setMsg(e.getMessage()).toJson();
        }
    }

    @ApiOperation(value="根据年份获取列表", notes="根据年份获取列表")
    @RequestMapping("/list")
    public String listBusiness(@RequestBody Holiday holiday){
        try {
            Page<Holiday> list = holidayService.listByYear(holiday.getYear(), holiday.getPageRequest());
            return resp.setStateCode(BaseResponse.SUCCESS).setData(list).toJson();
        }catch (Exception e){
            return resp.setStateCode(BaseResponse.ERROR).setMsg(e.getMessage()).toJson();
        }
    }

    @ApiOperation(value="测试API：根据year正负决定向前还是向后，以yyyymmdd（严格按照yyyy-mm-dd格式）为基准，取day天的工作日")
    @RequestMapping("/getWorkDay")
    public String getWorkDay(@RequestBody Holiday holiday){
        try {
            LocalDate date = holidayService.getWordDay(
                    LocalDate.parse(holiday.getYyyymmdd()),
                    holiday.getDay(),
                    holiday.getYear()>0);
            return resp.setStateCode(BaseResponse.SUCCESS).setData(date).toJson();
        }catch (Exception e){
            return resp.setStateCode(BaseResponse.ERROR).setMsg(e.getMessage()).toJson();
        }
    }
}
