package com.magustek.szjh.utils;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
public class KeyValueBean {
    private String key;
    private String value;
    private String opera = "M";
    private String calc = "";

    public KeyValueBean put(String key, String value){
        this.key = key;
        this.value = value;
        return this;
    }
    public KeyValueBean put(String key, String value, String opera){
        this.key = key;
        this.value = value;
        this.opera = opera;
        return this;
    }

    public static ArrayList<KeyValueBean> paresMap(Map<String, BigDecimal> map){
        ArrayList<KeyValueBean> list = new ArrayList<>(map.keySet().size());
        for( Map.Entry<String, BigDecimal> m : map.entrySet()){
            KeyValueBean keyValueBean = new KeyValueBean();
            keyValueBean.put(m.getKey(), m.getValue().toString());
            list.add(keyValueBean);
        }
        return list;
    }
}
