package com.magustek.szjh.config;

import lombok.Getter;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

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
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setRequestFactory(factory);

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        //设置账号密码
        restTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor(odataUser, odataPass));
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        // 长连接保持时长600秒
        PoolingHttpClientConnectionManager pollingConnectionManager= new PoolingHttpClientConnectionManager(600, TimeUnit.SECONDS);
        //最大连接数
        pollingConnectionManager.setMaxTotal(100);
        //单路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(100);
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // 重试次数2次，并开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        HttpClient httpClient = httpClientBuilder.build();
        // httpClient连接底层配置clientHttpRequestFactory
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // 连接超时时长配置
        clientHttpRequestFactory.setConnectTimeout(600*1000);//单位为ms
        // 数据读取超时时长配置
        clientHttpRequestFactory.setReadTimeout(600*1000);//单位为ms
        // 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        clientHttpRequestFactory.setConnectionRequestTimeout(2000);
        return clientHttpRequestFactory;
    }
}
