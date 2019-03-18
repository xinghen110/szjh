package com.magustek.szjh.task;

import com.magustek.szjh.basedataset.controller.BasedataSetController;
import com.magustek.szjh.config.InitConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitConfigDataTask {

    private InitConfigData initConfigData;
    private BasedataSetController basedataSetController;


    public InitConfigDataTask(InitConfigData initConfigData, BasedataSetController basedataSetController) {
        this.initConfigData = initConfigData;
        this.basedataSetController = basedataSetController;
    }

    //@Scheduled(cron = "0 0 */1 * * ?")
    /*public void executeInitConfig(){
        try {
            initConfigData.init();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        System.gc();
    }*/

    @Scheduled(cron = "0 0 3 * * ?")
    public void executeFetchBaseData(){
        synchronized (this){
            try {
                log.warn("计划任务{}开始执行，Thread id：{}，name：{}", "executeFetchBaseData", Thread.currentThread().getId(), Thread.currentThread().getName());
                long b = System.currentTimeMillis();
                basedataSetController.fetchBaseData(null);
                long e = System.currentTimeMillis();
                log.warn("计划任务{}执行完毕，耗时：{}s", "executeFetchBaseData", (e-b)/1000.00);
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            System.gc();
        }
    }
}
