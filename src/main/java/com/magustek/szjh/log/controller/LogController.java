package com.magustek.szjh.log.controller;

import com.magustek.szjh.log.entity.LogEntity;
import com.magustek.szjh.log.entity.LogEntityVO;
import com.magustek.szjh.log.service.LogService;
import com.magustek.szjh.utils.base.BaseResponse;
import com.magustek.szjh.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/log", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class LogController {
    private LogService logService;
    private BaseResponse resp;

    public LogController(LogService logService) {
        this.logService = logService;
        resp = new BaseResponse();
        log.info("初始化 LogController");
    }

    @RequestMapping("/search")
    public String getLog(@RequestBody LogEntityVO vo){
        Page<LogEntity> logEntities = logService.searchLog(vo);
        return resp.setStateCode(BaseResponse.SUCCESS).setMsg("").setData(logEntities).toJson();
    }

    @RequestMapping("/save")
    public String saveLog(@RequestBody LogEntityVO vo){
        LogEntity logEntity = logService.saveLog(vo);
        return resp.setStateCode(BaseResponse.SUCCESS).setMsg("").setData(logEntity).toJson();
    }

}
