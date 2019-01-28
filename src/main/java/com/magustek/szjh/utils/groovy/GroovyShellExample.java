package com.magustek.szjh.utils.groovy;

import com.google.common.base.Strings;
import com.magustek.szjh.utils.ClassUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;

public class GroovyShellExample {
    public static void main(String args[]) {
        String a = "";
        try {
            System.out.println(ClassUtils.dfYMD.parse("20180116"));
            c();
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
            binding.setVariable("G111", "ICM03");

            GroovyShell shell = new GroovyShell(binding);
            Object value = shell.evaluate("G111 != 'ICM09'");

            System.err.println(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void d(){
        String a = "a";
        a = a.substring(0, a.length()-1);
        System.out.println(a);
    }

    public static void e(){
        BigDecimal aa = new BigDecimal(2);
        BigDecimal bb = new BigDecimal(3);
        aa.add(bb);
        System.out.println(aa.toString());
    }

}
