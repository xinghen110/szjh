package com.magustek.szjh.log.controller;

/*@Slf4j
@RestController
@RequestMapping(value = "/log", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)*/
public class LogController {
/*    private LogService logService;
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
    }*/

}
