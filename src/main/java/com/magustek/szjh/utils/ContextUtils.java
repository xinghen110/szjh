package com.magustek.szjh.utils;

import com.magustek.szjh.user.bean.UserInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ContextUtils {
    /**
     * 获取当前用户
     * */
    public static String getUserName(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes!=null){
            HttpServletRequest request = requestAttributes.getRequest();
            if(request!=null){
                UserInfo user = (UserInfo)request.getSession().getAttribute("userInfo");
                if(user!=null){
                    return user.getLoginname();
                }

            }
        }
        return "";
    }
}
