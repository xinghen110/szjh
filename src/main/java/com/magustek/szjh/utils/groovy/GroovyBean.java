package com.magustek.szjh.utils.groovy;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GroovyBean {
    private Map<String, Object> binding;    //运行参数
    private String command;     //运行命令
    private Object result;      //运行结果
}
