package com.magustek.szjh.utils;

import com.google.common.base.Strings;
import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.configset.bean.IEPlanScreenItemSet;
import com.magustek.szjh.utils.base.BasePage;
import com.magustek.szjh.utils.constant.IEPlanSelectDataConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日期、时间工具类
 *
 * */
@Slf4j
@SuppressWarnings("unused")
public class ClassUtils {

    public final static DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    public final static DateFormat dfDateTime = new SimpleDateFormat("HH:mm:ss");
    public final static DateFormat dfFullTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public final static DateFormat dfYMD = new SimpleDateFormat("yyyyMMdd");
    public final static DateFormat dfHMS = new SimpleDateFormat("HHmmss");
    public final static DateFormat dfHM = new SimpleDateFormat("HHmm");
    public final static DateFormat dfYMDHMS = new SimpleDateFormat("HHmm");
    public final static DateFormat dfYYYY = new SimpleDateFormat("yyyy");

    // http header
    public final static String HTTP_HEADER = "application/json;charset=utf-8";

    public static String now(){
        return dfFullTime.format(Calendar.getInstance().getTime());
    }

    /**
     * 将字符串数值增加plus后，返回字符串
     *
     * @param s     待处理的字符串
     * @param plus  需要增加的数值
     *
     * @return s增加plus后的字符串结果，例如：s="30", plus=1, return="31"
     * */
    public static String stringPlus(String s, int plus){
        long i = Long.parseLong(s);
        i += plus;
        return Long.toString(i);
    }

    /**
     * 判断列表是否为空
     *
     * @param o    待判断的列表
     *
     * @return 如果列表为空，则返回true
     * */
    public static boolean isEmpty(Object[] o) {
        return o == null || o.length < 1;
    }
    public static boolean isEmpty(BigDecimal b) {
        return b == null || b.compareTo(BigDecimal.ZERO) == 0;
    }
    /**
     * 判断列表是否为空
     *
     * @param l    待判断的列表
     *
     * @return 如果列表为空，则返回true
     * */
    public static boolean isEmpty(Map l) {
        return l == null || l.size() < 1;
    }
    /**
     * 判断列表是否为空
     *
     * @param l    待判断的列表
     *
     * @return 如果列表为空，则返回true
     * */
    public static boolean isEmpty(Collection l) {
        return l == null || l.size() < 1;
    }
    /**
     * 判断版本是否为空
     *
     * @param version    待判断的版本
     *
     * @return 如果列表为空，则返回当天的字符串
     * */
    public static String checkVersion(String version) {
        if(Strings.isNullOrEmpty(version)){
            return LocalDate.now().toString();
        }else{
            return version;
        }
    }

    /**
     * 判断Long是否为空
     *
     * @param l    待判断的列表
     *
     * @return 如果列表为空，则返回true
     * */
    public static boolean isEmpty(Long l) {
        return l == null || l.equals(0L);
    }

    /**
     * 根据条件计算日期
     * @param baseDate  基准日期
     * @param type      类型（y/m/d）
     * @param value     日期量
     * @param forward   true向后/false向前
     * @return          计算后的日期
     */
    public static LocalDate getDate(LocalDate baseDate, String type, Integer value, boolean forward){
        switch (type) {
            case "Y":
                if(forward){
                    baseDate = baseDate.plusYears(value);
                }else{
                    baseDate = baseDate.minusYears(value);
                }
                break;
            case "M":
                if(forward){
                    baseDate = baseDate.plusMonths(value);
                }else{
                    baseDate = baseDate.minusMonths(value);
                }
                break;
            case "D":
                if(forward){
                    baseDate = baseDate.plusDays(value);
                }else{
                    baseDate = baseDate.minusDays(value);
                }
                break;
            default: log.error("日期类型【"+type+"】，格式不正确！");
        }
        return baseDate;
    }

    /**
     * 根据条件计算日期
     * @param baseDate  基准日期
     * @param format    类型（y/m/d）
     * @return          计算后的日期
     */
    public static String formatDate(LocalDate baseDate, String format) {
        DateTimeFormatter formatter;
        switch (format) {
            case "Y":
                formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                break;
            case "M":
                formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                break;
            case "D":
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                break;
            default:
                log.error("日期类型【"+format+"】，格式不正确！");
                return "";
        }
        return baseDate.format(formatter);
    }

    /**
     * 根据条件计算日期
     * @param baseDate  基准日期
     * @param format    类型（y/m/d）
     * @return          计算后的日期
     */
    public static String shortFormatDate(LocalDate baseDate, String format){
        DateTimeFormatter formatter;
        switch (format) {
            case "Y":
                formatter = DateTimeFormatter.ofPattern("yyyy");
                break;
            case "M":
                formatter = DateTimeFormatter.ofPattern("M");
                break;
            case "D":
                formatter = DateTimeFormatter.ofPattern("dd");
                break;
            default:
                log.error("日期类型【"+format+"】，格式不正确！");
                return "";
        }
        return baseDate.format(formatter);
    }

    /**
     * yyyyMMdd转LocalDate
     * @param yyyyMMdd    yyyyMMdd
     * @return          计算后的日期
     */
    public static LocalDate StringToLocalDate(String yyyyMMdd) throws ParseException {
        Date parse = dfYMD.parse(yyyyMMdd);
        Instant instant = parse.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * yyyyMMdd转LocalDate
     * @param yyyyMMdd    yyyyMMdd
     * @return          计算后的日期
     */
    public static LocalDate StringToLocalDateWithoutException(String yyyyMMdd) {
        try {
            return StringToLocalDate(yyyyMMdd);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
    }
    /**
     * yyyyMMdd转LocalDate
     * @param yyyyMMdd    yyyyMMdd
     * @return          计算后的日期
     */
    public static String StringToLocalDateString(String yyyyMMdd) {
        try {
            return StringToLocalDate(yyyyMMdd).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }

    /**
     * 将对象转换为map json格式，处理其中keyValueBean。
     * @param superClass    是否包含父类字段
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> coverToMapJson(Object o, String keyValueBeanName, String qcode, boolean superClass) {
        ArrayList<Field> fList = new ArrayList<>();
        //递归遍历获取所有父类的字段
        Class clazz = o.getClass();
        if(superClass){
            for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
                fList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            }
        }else{
            fList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }


        //根据字段名获取值
        Map<String, String> map = new HashMap<>();
        try{
            for(Field f : fList){
                String name = f.getName();
                Method method = o.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                if(!name.equals(keyValueBeanName)) {
                    Object value = method.invoke(o);
                    if(value != null){
                        map.put(name, value.toString());
                    }
                }else{
                    Collection<KeyValueBean> list = (Collection<KeyValueBean>) method.invoke(o);
                    for(KeyValueBean bean : list){
                        map.put(bean.getKey(),handlePunit(bean.getValue(), qcode));
                    }
                }
            }
        }catch (Exception e){
            log.error("json转换错误："+ e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    //处理价格单位问题
    public static String handlePunit(String price, String qcode){
        try{
            BigDecimal value = new BigDecimal(price);
            switch (qcode){
                case "UN01": //元
                    return value.setScale(2,BigDecimal.ROUND_HALF_DOWN).toString();
                case "UN02": //万元
                    return value.divide(new BigDecimal(10000),2,BigDecimal.ROUND_HALF_DOWN).toString();
                case "UN03": //亿元
                    return value.divide(new BigDecimal(10000*10000),2,BigDecimal.ROUND_HALF_DOWN).toString();
                default:
                    return price;
            }
        }catch (NumberFormatException e){
            return price;
        }
    }

    public static BigDecimal coverStringToBigDecimal(String s){
        try{
            return new BigDecimal(s);
        }catch (NumberFormatException e){
            return BigDecimal.ZERO;
        }
    }

    public static List<Map<String, String>> coverBeanToMapWithSdvarMap(List itemList,
                                                  Map<String, List<IEPlanSelectValueSet>> selectValueMap,
                                                  Map<String, List<IEPlanScreenItemSet>> sdvarMap){
        List<Map<String, String>> list = new ArrayList<>();
        //getter方法列表
        Map<String, Method> methodList = new HashMap<>(sdvarMap.size());
        sdvarMap.forEach((key,value)->{
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(itemList.get(0).getClass(), key);
            //如果getter方法不为空，则需要从itemList里取值。
            if(propertyDescriptor != null){
                methodList.put(key, propertyDescriptor.getReadMethod());
            }
        });
        //获取合同流水号方法
        PropertyDescriptor htsnoPD = BeanUtils.getPropertyDescriptor(itemList.get(0).getClass(), "htsno");
        Method htsnoMethod;
        if(htsnoPD==null){
            return list;
        }else {
            htsnoMethod = htsnoPD.getReadMethod();
        }
        for(Object item : itemList){
            Map<String, String> map = new HashMap<>(sdvarMap.size());
            list.add(map);
            sdvarMap.forEach((k,v)->{
                try {
                    //构造临时变量
                    IEPlanSelectValueSet temp = new IEPlanSelectValueSet();
                    temp.setSdart(k);
                    if(methodList.containsKey(k)){
                        //从计划表里取数据
                        temp.setSdval(methodList.get(k).invoke(item).toString());

                    }else{
                        //从取数表里取数据
                        String htsno = htsnoMethod.invoke(item).toString();
                        //容错处理
                        if(!Strings.isNullOrEmpty(htsno)){
                            List<IEPlanSelectValueSet> htsnoList = selectValueMap.get(htsno);
                            if(!isEmpty(htsnoList)){
                                Map<String, List<IEPlanSelectValueSet>> sdartMap = htsnoList.stream().collect(Collectors.groupingBy(IEPlanSelectValueSet::getSdart));
                                if(!isEmpty(sdartMap)){
                                    List<IEPlanSelectValueSet> sdartList = sdartMap.get(k);
                                    if(!isEmpty(sdartList)){
                                        temp = sdartList.get(0);
                                    }
                                }
                            }
                        }
                    }
                    handleDate(map, sdvarMap, temp);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                    e.printStackTrace();
                }
            });
        }
        return list;
    }

    /**
     * 日期类型，需要特殊处理
     * */
    public static synchronized void handleDate(Map<String, String> map, Map<String, List<IEPlanScreenItemSet>> sdvarMap, IEPlanSelectValueSet item ){
        if(IEPlanSelectDataConstant.RESULT_TYPE_DATS.equals(sdvarMap.get(item.getSdart()).get(0).getVtype())){
            map.put(item.getSdart(), ClassUtils.StringToLocalDateString(item.getSdval()));
        }else{
            map.put(item.getSdart(), item.getSdval());
        }
    }

    public static Page<Map<String, String>> constructPage(BasePage page, List<Map<String, String>> list){
        long l = System.currentTimeMillis();
        int start = page.getPageRequest().getOffset();
        int pageSize = page.getPageRequest().getPageSize();
        int end = start+pageSize > list.size()? list.size() : start+pageSize;
        PageImpl<Map<String, String>> mapPage = new PageImpl<>(list.subList(start, end), page.getPageRequest(), list.size());
        log.warn("分页耗时{}秒", (System.currentTimeMillis()-start) / 1000.00);
        return mapPage;
    }
}
