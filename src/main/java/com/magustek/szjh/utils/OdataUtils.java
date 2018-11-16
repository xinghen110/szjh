package com.magustek.szjh.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class OdataUtils {
    public static final String Token = "ZCM_ODATA_ORG_SRV/PartnerAddressSet";
    public static final String UserLogonSet = "ZCM_ODATA_ORG_SRV/UserLogonSet";
    public static final String IEPlanOperationSet = "ZCM_ODATA_IEPLAN_SRV/IEPlanOperationSet";
    public static final String IEPlanCalculationSet = "ZCM_ODATA_IEPLAN_SRV/IEPlanCalculationSet";
    public static final String IEPlanDimensionSet = "ZCM_ODATA_IEPLAN_SRV/IEPlanDimensionSet";
    public static final String IEPlanSelectDataSet = "ZCM_ODATA_IEPLAN_SRV/IEPlanSelectDataSet";

    /**
     * 调用odata返回多条记录： 以实体返回
     * getResults(这里用一句话描述这个方法的作用)
     * @param  result    odata调用结果
     * @param  entity    设定文件
     * @return String    List对象
     */
    public static <T> List<T> getResultsWithEntity(String result, Class<T> entity) {
        if (result == null) {
            return null;
        }
        JSONObject jSONObject1 = JSON.parseObject(result);
        JSONObject jSONObject2 = (JSONObject) jSONObject1.get("d");
        Object object = jSONObject2.get("results");
        String str = JSON.toJSONString(object);
        return JSON.parseArray(str, entity);
    }
}
