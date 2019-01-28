package com.magustek.szjh.utils.constant;

/**
 * 允许匿名访问列表
 *
 * */
public class AllowResource {
    public static String[] resource = {
            "/static/**",//static 文件夹内b文件夹（静态资源）所有资源均允许匿名
            "/manage/**",//tomcat管理资源允许匿名访问
            "/swagger-ui.html/**",//restapi允许匿名访问
            "/swagger-resources/**",//restapi允许匿名访问
            "/webjars/**",//restapi允许匿名访问
            "/v2/**",//restapi允许匿名访问
            "/**/*.html",
            "/**/*.htm",
            "/**/*.gif",
            "/**/*.jpeg",
            "/**/*.jpg",
            "/**/*.js",
            "/**/*.css",
            "/**/*.*",
            "/**/*.font",
            "#/login",
            "/"
    };
}
