package com.magustek.szjh.log.entity;


import com.magustek.szjh.utils.ContextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
public class LogEntity {

    //@Id
    private String _id;
    private String loginName;
    private String userName;
    private String time;
    private String level;
    private String logger;
    private String thread;
    private String message;

    public LogEntity() {
        //获取当前用户信息
        this.userName = ContextUtils.getUserName();

        if(this.userName == null){
            this.loginName = "system";
            this.userName = "系统日志";
        }
    }
}
