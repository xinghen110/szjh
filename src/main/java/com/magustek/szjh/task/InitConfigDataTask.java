package com.magustek.szjh.task;

import com.magustek.szjh.basedataset.controller.BasedataSetController;
import com.magustek.szjh.report.bean.vo.ReportVO;
import com.magustek.szjh.report.service.StatisticalReportService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.RedisUtil;
import com.magustek.szjh.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class InitConfigDataTask implements DisposableBean {

    @Value("${schedule.executeFetchBaseData}")
    public String executeFetchBaseData;
    private BasedataSetController basedataSetController;
    private RedisTemplate<String, String> redisTemplate;
    private StatisticalReportService statisticalReportService;
    private RedisUtil redisUtil;


    public InitConfigDataTask(BasedataSetController basedataSetController, RedisTemplate<String, String> redisTemplate, StatisticalReportService statisticalReportService, RedisUtil redisUtil) {
        this.basedataSetController = basedataSetController;
        this.redisTemplate = redisTemplate;
        this.statisticalReportService = statisticalReportService;
        this.redisUtil = redisUtil;
    }

    //@Scheduled(cron = "10 * * * * ?")
    public void executeInitConfig(){
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("executeInitConfig", "X");
        if(flag){
            redisTemplate.expire("executeInitConfig", 1, TimeUnit.HOURS);
        }else{
            return;
        }
        try {
            for(int i=0;i<10;i++){
                log.warn("schedule {}",i);
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        redisTemplate.delete("executeInitConfig");
        System.gc();
    }

    //@Scheduled(cron = "0 0 3 * * ?")
    @Scheduled(cron = "#{initConfigDataTask.executeFetchBaseData}")
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

        try {
            //更新缓存
            ReportVO reportVO = new ReportVO();
            reportVO.setVersion(ClassUtils.version(null));
            redisUtil.deleteKey(StatisticalReportService.getOutputTaxDetailByVersion);
            statisticalReportService.getOutputTaxDetailByVersion(reportVO);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        //关闭线程或线程池
        log.warn("关闭线程池");
        ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) SpringUtils.getBean("scheduler");
        scheduler.shutdown();
        log.warn("线程池已关闭");
    }
}
