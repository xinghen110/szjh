package com.magustek.szjh.user.service;

import com.magustek.szjh.user.bean.CompanyModel;
import com.magustek.szjh.user.bean.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserInfoService extends UserDetailsService {
    UserInfo userLogin(String Loginname, String Password, String Aflag);
    UserInfo getUserByLoginName(String Loginname);
    List<CompanyModel> getCompanyModelList(String loginName, String phone);
}
