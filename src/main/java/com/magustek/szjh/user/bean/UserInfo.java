package com.magustek.szjh.user.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户信息
 * */
@Getter
@Setter
@NoArgsConstructor
public class UserInfo implements Serializable{
    private String Loginname;           //登录用户名
    private String Username;            //真实用户名
    private String Password;            //密码
    private String Phone;               //手机号码
    private String Ustyp;               //用户类型
    private String Usersource;          //用户来源
    private String Msgtype;             //消息类型
    private String Aflag;               //动作标识
    private String Msgtext;             //消息文本

    public static UserInfo paresMap(Map<String, Object> result){
        UserInfo user;
        if( result!=null){
            if(!((String)result.get("Msgtype")).equalsIgnoreCase("S")){
                return null;
            }
            user = new UserInfo();
            user.setLoginname((String) result.get("Loginname"));
            user.setUsername((String) result.get("Username"));
            user.setPassword((String) result.get("Password"));
            user.setPhone((String) result.get("Phone"));
            user.setUstyp((String) result.get("Ustyp"));
            user.setUsersource((String) result.get("Usersource"));
            user.setMsgtype((String) result.get("Msgtype"));
            user.setAflag((String) result.get("Aflag"));
            user.setMsgtext((String) result.get("Msgtext"));
            return user;
        }
        return null;
    }
}
