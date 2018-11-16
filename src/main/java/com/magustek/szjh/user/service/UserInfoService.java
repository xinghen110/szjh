package com.magustek.szjh.user.service;

import com.magustek.szjh.user.bean.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserInfoService extends UserDetailsService {
    UserInfo userLogin(String Loginname, String Password, String Aflag);
    UserInfo getUserByLoginName(String Loginname);
}
