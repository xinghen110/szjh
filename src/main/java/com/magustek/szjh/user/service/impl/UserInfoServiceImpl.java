package com.magustek.szjh.user.service.impl;

import com.magustek.szjh.config.HttpConnectConfig;
import com.magustek.szjh.user.bean.CompanyModel;
import com.magustek.szjh.user.bean.UserInfo;
import com.magustek.szjh.user.service.UserInfoService;
import com.magustek.szjh.user.service.UserInfoServiceOdata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    private UserInfoService userInfoService;

    @Autowired
    public UserInfoServiceImpl(UserInfoServiceOdata userInfoServiceOdata, HttpConnectConfig httpConnectConfig) throws Exception{
        if("http".equals(httpConnectConfig.getZuser_service())){
            this.userInfoService = userInfoServiceOdata;
        }else{
            log.error("zuser_service 参数配置错误！" + httpConnectConfig.getZuser_service());
            throw new Exception("zuser_service 参数配置错误！" + httpConnectConfig.getZuser_service());
        }
    }

    @Override
    public UserInfo userLogin(String Loginname, String Password, String Aflag) {
        return userInfoService.userLogin(Loginname, Password, Aflag);
    }

    @Override
    public UserInfo getUserByLoginName(String Loginname) {
        return userInfoService.getUserByLoginName(Loginname);
    }

    @Override
    public List<CompanyModel> getCompanyModelList(String loginName, String phone) {
        return userInfoService.getCompanyModelList(loginName, phone);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userInfoService.loadUserByUsername(username);
    }
}
