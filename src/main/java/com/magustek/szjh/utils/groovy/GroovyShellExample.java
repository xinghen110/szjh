package com.magustek.szjh.utils.groovy;

import com.google.common.collect.Lists;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.KeyValueBean;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GroovyShellExample {
    public static void main(String[] args) {
        try {
            System.out.println(ClassUtils.dfYMD.parse("20180116"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        c();
    }

    public static void a(){
        Binding binding = new Binding();
        binding.setVariable("x", 10);
        binding.setVariable("language", "Groovy");

        GroovyShell shell = new GroovyShell(binding);
        Object value = shell.evaluate("println \"Welcome to $language\"; y = x * 2; z = x * 3; return x ");

        System.err.println(value +", " + value.equals(10));
        System.err.println(binding.getVariable("y") +", " + binding.getVariable("y").equals(20));
        System.err.println(binding.getVariable("z") +", " + binding.getVariable("z").equals(30));
    }

    public static void b(){
        Binding binding = new Binding();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            binding.setVariable("x", format.parse("20180101"));
            binding.setVariable("y", format.parse("20181208"));

            GroovyShell shell = new GroovyShell(binding);
            Object value = shell.evaluate("y-x");

            System.err.println(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void c(){
        Binding binding = new Binding();
        try {
            BigDecimal g131 = new BigDecimal("0.00");

            binding.setVariable("G131", g131);
            binding.setVariable("G201", "03");
            GroovyShell shell = new GroovyShell(binding);
            Object value = shell.evaluate("G201!='01' && G131.toBigDecimal().compareTo(BigDecimal.ZERO) >0");

            System.err.println(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void d(){
        String[] s = {"713.00-","789.00"};
        for (String s1 : s) {
            if(s1.charAt(s1.length()-1) == '-'){
                s1 = "-"+s1.substring(0,s1.length()-1);
            }
            System.out.println(s1);
        }
    }

    public static void e(){
        KeyValueBean bean = new KeyValueBean();
        bean.put("ke123y", "value", "opera");
        PropertyDescriptor key = BeanUtils.getPropertyDescriptor(bean.getClass(), "key");
        try {
            System.out.println(key.getReadMethod().invoke(bean));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static void f(){
        BigDecimal bigDecimal = new BigDecimal(0.00);
        System.out.println(bigDecimal.compareTo(BigDecimal.ZERO));
    }

    private static void g(){
        List<String> s = Arrays.asList("201901011","20190102","20190105","20190108","20190103","20190101","");
        s = s.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        s.forEach(System.out::println);
    }

    private static void h(){
        System.out.println("2019-01-11".compareTo("")>0);
    }

    private static void i(){
        BigDecimal d = new BigDecimal(0.61D);
        Double a = 0.68;
        double b =  0.1261D;
        double e =  0.128D;
        double f =  b*e;
        System.out.println(d);
        System.out.println(d.setScale(4, BigDecimal.ROUND_HALF_DOWN).toString());
        System.out.println(f);
    }

    private static void j(){
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(1556841600L*1000);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(1556870400L*1000);
        System.out.println("s:"+start.getTime().toString());
        System.out.println("e:"+end.getTime().toString());
    }

}
