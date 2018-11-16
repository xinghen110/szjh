package com.magustek.szjh.config;

import com.magustek.szjh.user.service.UserInfoService;
import com.magustek.szjh.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
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
    @Value("${zuser.service}")
    private String zuser_service;
    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.warn("初始化完成！" + zuser_service);
    }
}
