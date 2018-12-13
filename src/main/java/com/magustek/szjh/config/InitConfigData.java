package com.magustek.szjh.config;

import com.magustek.szjh.configset.controller.IEPlanConfigSetController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("InitConfigData")
public class InitConfigData {
    private IEPlanConfigSetController iePlanConfigSetController;

    public InitConfigData(IEPlanConfigSetController iePlanConfigSetController) {
        this.iePlanConfigSetController = iePlanConfigSetController;
    }


    public void init() throws Exception{
        try {
            long start = System.currentTimeMillis();
            log.warn("配置信息初始化开始！");
            iePlanConfigSetController.initAll();
            log.warn("配置信息初始化完成！耗时："+((System.currentTimeMillis()-start)/1000.00)+"秒");
        } catch (Exception e) {
            log.error("配置信息初始化出错！"+e.getMessage());
            throw e;
        }
    }
}
