package com.magustek.szjh.utils.groovy;

import com.google.common.base.Strings;
import com.magustek.szjh.utils.ClassUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GroovyShellExample {
    public static void main(String args[]) {
        String a = "";
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
            binding.setVariable("x", "abc");

            GroovyShell shell = new GroovyShell(binding);
            Object value = shell.evaluate("x==\"abc\"");

            System.err.println(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
