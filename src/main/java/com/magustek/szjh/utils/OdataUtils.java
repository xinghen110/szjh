package com.magustek.szjh.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class OdataUtils {
    public static final String Token = "ZCM_ODATA_ORG_SRV/PartnerAddressSet";

    public static final String UserLogonSet = "ZCM_ODATA_ORG_SRV/UserLogonSet";
    public static final String UserAuthSet = "ZCM_ODATA_ORG_SRV/UserAuthSet";
    public static final String OrgInforSet = "ZCM_ODATA_ORG_SRV/OrgInforSet";
    public static final String ConfigDataSource = "ZCM_ODATA_CONFIG_SRV/ConfigDataSourceSet";
    public static final String OrginazationSet = "ZCM_ODATA_ORG_SRV/OrginazationSet";

    public static final String IEPlanOperationSet = "ZCM_ODATA_PLAN_SRV/IEPlanOperationSet";
    public static final String IEPlanCalculationSet = "ZCM_ODATA_PLAN_SRV/IEPlanCalculationSet";

    public static final String IEPlanReportHeadSet = "ZCM_ODATA_PLAN_SRV/IEPlanReportHeadSet";
    public static final String IEPlanReportItemSet = "ZCM_ODATA_PLAN_SRV/IEPlanReportItemSet";
    public static final String IEPlanStatisticSet = "ZCM_ODATA_PLAN_SRV/IEPlanStatisticSet";

    public static final String IEPlanSelectDataSet = "ZCM_ODATA_PLAN_SRV/IEPlanSelectDataSet";
    public static final String IEPlanSelectValueSet = "ZCM_ODATA_PLAN_SRV/IEPlanSelectValueSet";

    public static final String IEPlanDimensionSet = "ZCM_ODATA_PLAN_SRV/IEPlanDimensionSet";
    public static final String IEPlanDimenValueSet = "ZCM_ODATA_PLAN_SRV/IEPlanDimenValueSet";

    public static final String IEPlanPaymentSet = "ZCM_ODATA_PLAN_SRV/IEPlanPaymentSet";
    public static final String IEPlanTermsSet = "ZCM_ODATA_PLAN_SRV/IEPlanTermsSet";

    public static final String IEPlanBusinessHeadSet = "ZCM_ODATA_PLAN_SRV/IEPlanBusinessHeadSet";
    public static final String IEPlanBusinessItemSet = "ZCM_ODATA_PLAN_SRV/IEPlanBusinessItemSet";

    //public static final String IEPlanContractHeadSet = "ZCM_ODATA_PLAN_SRV/IEPlanContractHeadSet";

    /**
     * 调用odata返回多条记录： 以实体返回
     * @param  result    odata调用结果
     * @param  entity    设定文件
     * @return String    List对象
     */
    public static <T> List<T> getListWithEntity(String result, Class<T> entity) throws Exception {
        return getListWithEntity(result, entity, null, "yyyy-MM-dd");
    }

    /**
     * 调用odata返回多条记录： 以实体返回
     * @param  result    odata调用结果
     * @param  entity    设定文件
     * @return String    List对象
     */
    public static <T> List<T> getListWithEntity(String result, Class<T> entity,String[] keys,String pattern) throws Exception {
        if (Strings.isNullOrEmpty(result)) {
            return new ArrayList<>();
        }
        JSONObject jSONObject1 = JSON.parseObject(result);
        JSONObject jSONObject2 = (JSONObject) jSONObject1.get("d");
        Object object = jSONObject2.get("results");
        String str = JSON.toJSONString(object);
        //处理日期类型
        str = formatDateByPattern(str,keys,pattern);
        return JSON.parseArray(str, entity);
    }

    /**
     * 调用odata返回单条记录 ： 以实体返回
     * @param   result   odata返回字符串
     * @return <T> T   组装好的实体类
     */
    public static <T> T getSingleWithEntity(String result, Class<T> entity) {

        JSONObject jSONObject = JSON.parseObject(result);
        JSONObject object = jSONObject.getJSONObject("d");
        return object.toJavaObject(entity);
    }

    /**
     * 替换odata接口返回的日期字符串为指定格式的日期字符串（OData日期格式："/Date(1498780800000)/"）
     * @param jsonStr json格式字符串
     * @param keys 指定日期键
     * @param pattern 日期格式
     */
    private static String formatDateByPattern(String jsonStr,String[] keys,String pattern) throws Exception {
        if (Strings.isNullOrEmpty(pattern)) {
            return jsonStr;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return formatJsonField(jsonStr, keys, (value) -> {
            String subValue = value==null?null:value.substring(6,value.length()-2);
            return subValue==null?null:simpleDateFormat.format(new Date(Long.parseLong(subValue)));
        });
    }

    /**
     * 根据指定逻辑替换指定key对应的value值
     * @param jsonStr json格式字符串
     * @param keys 指定键
     * @param function 处理value的逻辑
     */
    private static String formatJsonField(String jsonStr,String[] keys,Function<String, String> function) throws Exception{
        if (Strings.isNullOrEmpty(jsonStr)) {
            return "";
        }
        if (ClassUtils.isEmpty(keys)) {
            return jsonStr;
        }
        Object o = JSONObject.parse(jsonStr);
        if (o instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) o;
            //单条记录
            handleValue(keys, function, jsonObject);
            return jsonObject.toJSONString();
        }else if(o instanceof JSONArray){
            JSONArray array = (JSONArray) o;
            //列表
            array.iterator().forEachRemaining((result) -> handleValue(keys, function, (JSONObject) result));
            return array.toJSONString();
        }else{
            throw new Exception("Odata 返回数据有误！:"+o.toString());
        }
    }
    private static JSONObject handleValue(String[] keys,Function<String, String> function, JSONObject d){
        for (String key : keys) {
            String value = d.getString(key);
            if (null != value) {
                String formatValue = function.apply(value);//格式化处理原始value值
                d.put(key, formatValue);
            }
        }
        return d;
    }
}
