package com.magustek.szjh.utils.http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "zconnection") // 将所有app前缀的属性，自动赋值给对应的Bean属性
public class HttpConfig {
    private String type;
    private String odataIp;
    private String odataPort;
    private String odataUser;
    private String odataPasswd;
    private String odataClient;
    private String esbIp;
}
