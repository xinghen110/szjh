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


    public void init(){
        iePlanConfigSetController.initAll();
    }
}
