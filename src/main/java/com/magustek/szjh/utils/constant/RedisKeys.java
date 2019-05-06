package com.magustek.szjh.utils.constant;


import java.util.HashMap;
import java.util.Map;

public class RedisKeys {
    public static final String ORG_MAP = "org_map";
    public static final String ZB_MAP = "zb_map";
    public static final String ConfigDataSourceSet = "ConfigDataSourceSet";
    public static final String ExecuteData = "ExecuteData";
    public static final String SelectValueSet = "SelectValueSet";
    public static final String RollPlanHeadDataArchiveVOList = "RollPlanHeadDataArchiveVOList";

    public static Map<String, Long> getKeys(){
        Map<String, Long> map = new HashMap<>();
        map.put(ORG_MAP, -1L);
        map.put(ZB_MAP, -1L);
        map.put(ConfigDataSourceSet, -1L);
        map.put(ExecuteData, 60*60*2L);
        map.put(SelectValueSet, 60*60*2L);
        map.put(RollPlanHeadDataArchiveVOList, 60*60*2L);
        return map;
    }
}
