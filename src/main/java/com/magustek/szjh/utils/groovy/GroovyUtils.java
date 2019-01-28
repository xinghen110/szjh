package com.magustek.szjh.utils.groovy;

import com.google.common.base.Strings;
import com.magustek.szjh.utils.ClassUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GroovyUtils {
    private GroovyShell shell;
    private static Map<String, Script> scripts;

    public GroovyUtils(){
        this.shell = new GroovyShell();
        if(ClassUtils.isEmpty(scripts)){
            scripts = new HashMap<>();
        }
    }
    //执行运算
    public List<GroovyBean> exec(List<GroovyBean> list){
        long l1 = System.currentTimeMillis();
        if(ClassUtils.isEmpty(list)){
            return null;
        }
        list.forEach(bean->{
            Script script = getScript(bean.getCommand());
            synchronized (getScript(bean.getCommand())) {
                script.setBinding(getBinding(bean.getBinding()));
                try{
                    bean.setResult(script.run());
                }catch (Exception e){
                    log.warn(e.getMessage());
                }
            }
        });
        long l2 = System.currentTimeMillis();
        log.warn("Groovy计算完成，耗时：{}秒。", ((l2-l1)/1000.00));
        return list;
    }

    //执行运算
    public Object exec(GroovyBean bean){
        if(Strings.isNullOrEmpty(bean.getCommand())){
            return null;
        }
        Script script;
        synchronized (script = getScript(bean.getCommand())) {
            script.setBinding(getBinding(bean.getBinding()));
            try {
                return script.run();
            } catch (Exception e) {
                log.warn(e.getMessage());
                return null;
            }
        }
    }

    //缓存执行脚本
    private Script getScript(final String expression) {
        Script script;
        if (scripts.containsKey(expression)) {
            script = scripts.get(expression);
        } else {
            script = shell.parse(expression);
            scripts.put(expression, script);
        }
        return script;
    }
    //构造变量
    private Binding getBinding(final Map<String, Object> varList) {
        Binding binding = new Binding();
        varList.forEach(binding::setVariable);
        return binding;
    }
}
