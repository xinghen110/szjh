package com.magustek.szjh.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 初始化方法
 *
 * */
@Slf4j
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private InitConfigData initConfigData;

    @Value("${zuser.service}")
    private String zuser_service;

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event){
        log.warn("初始化开始！");
        try {
            if(initConfigData!=null){
                //initConfigData.init();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.warn("初始化完成！");
    }

}
