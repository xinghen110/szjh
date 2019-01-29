package com.magustek.szjh.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * ESB/Odata配置
 * */
@Configuration
@Getter
public class HttpConnectConfig {
    @Value("${zuser.service}")
    private String zuser_service;
    @Value("${zconnection.type}")
    private String type;
    @Value("${zconnection.odataIp}")
    private String odataIp;
    @Value("${zconnection.odataPort}")
    private String odataPort;
    @Value("${zconnection.odataUser}")
    private String odataUser;
    @Value("${zconnection.odataPasswd}")
    private String odataPass;
    @Value("${zconnection.odataClient}")
    private String odataClient;
    @Value("${zconnection.esbIp}")
    private String esbIp;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory ();
        factory.setReadTimeout(600*1000);//单位为ms
        factory.setConnectTimeout(600*1000);//单位为ms
        return factory;
    }
}
