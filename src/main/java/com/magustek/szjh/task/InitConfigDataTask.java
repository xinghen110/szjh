package com.magustek.szjh.task;

import com.magustek.szjh.config.InitConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitConfigDataTask {

    private InitConfigData initConfigData;


    public InitConfigDataTask(InitConfigData initConfigData) {
        this.initConfigData = initConfigData;
    }

    //@Scheduled(cron = "0 0 */1 * * ?")
    public void executeInitConfig(){
        try {
            initConfigData.init();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        System.gc();
    }
}
