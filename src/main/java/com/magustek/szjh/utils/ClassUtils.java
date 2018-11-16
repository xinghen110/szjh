package com.magustek.szjh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 日期、时间工具类
 *
 * */

@SuppressWarnings("unused")
public class ClassUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);

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
     * @param l    待判断的列表
     *
     * @return 如果列表为空，则返回true
     * */
    public static boolean isEmpty(List l) {
        return l == null || l.size() < 1;
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
}
