package com.magustek.szjh.utils.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GroovyScriptDemo {
    private static GroovyScriptCachingBuilder groovyScriptCachingBuilder = new GroovyScriptCachingBuilder();
    private Map<String, Object> variables = new HashMap<>();

    public GroovyScriptDemo() {
        this(Collections.<String, Object>emptyMap());
    }

    public GroovyScriptDemo(final Map<String, Object> contextVariables) {
        variables.putAll(contextVariables);
    }

    public void setVariables(final Map<String, Object> answers) {
        variables.putAll(answers);
    }

    public void setVariable(final String name, final Object value) {
        variables.put(name, value);
    }

    public Object evaluateExpression(String expression) {
        final Binding binding = new Binding();
        for (Map.Entry<String, Object> varEntry : variables.entrySet()) {
            binding.setProperty(varEntry.getKey(), varEntry.getValue());
        }
        Script script = groovyScriptCachingBuilder.getScript(expression);
        synchronized (script) {
            script.setBinding(binding);
            return script.run();
        }
    }
    public static void main(String args[]) {
        int i = 1000;
        while(i-->0) {
            GroovyScriptDemo demo = new GroovyScriptDemo();
            demo.setVariable("x", i);
            Object o = demo.evaluateExpression("return x>0");
            System.out.println(o.toString());
        }
        //b();
    }
}

class GroovyScriptCachingBuilder {
    private GroovyShell shell = new GroovyShell();
    private Map<String, Script> scripts = new HashMap<>();

    public Script getScript(final String expression) {
        Script script;
        if (scripts.containsKey(expression)) {
            script = scripts.get(expression);
        } else {
            script = shell.parse(expression);
            scripts.put(expression, script);
        }
        return script;
    }
}
