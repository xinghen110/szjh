package com.magustek.szjh.utils;

import com.google.common.base.Strings;
import com.magustek.szjh.user.bean.CompanyModel;
import com.magustek.szjh.user.bean.UserInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    /**
     * 获取当前用户
     * */
    public static UserInfo getUserInfo() throws Exception{
        HttpSession session = getSession();
        if(session!=null){
            UserInfo user = (UserInfo)session.getAttribute("userInfo");
            if(user!=null){
                return user;
            }
        }
        throw new Exception("获取用户信息失败！");
    }
    /**
     * 获取当前用户登录的公司
     * */
    public static CompanyModel getCompany() throws Exception{
        UserInfo userInfo = getUserInfo();
        if(userInfo!=null){
            CompanyModel companyModel = userInfo.getCompanyModel();
            if(companyModel!=null && !Strings.isNullOrEmpty(companyModel.getOrgcode())){
                return companyModel;
            }
        }
        throw new Exception("获取当前登录公司信息失败！");
    }
    /**
     * 获取Session
     * */
    public static HttpSession getSession(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes!=null){
            HttpServletRequest request = requestAttributes.getRequest();
            if(request!=null){
                return request.getSession();
            }
        }
        return null;
    }
}
