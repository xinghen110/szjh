package com.magustek.szjh.user.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CompanyModel implements Serializable{
    private String Phone;          //手机号码
    private String OrgName;      //组织机构描述
    private String Actorcode;  //职位编号
    private String Zzwmc;	   //职位名称
    private String Deptcode;    //部门编号
    private String Gtext;	   //部门名称
    private String Orgcode;      //组织机构编号
    private String Usersource;//用户来源(I - 内部    E - 外部)
    private String Loginname;  //登录用户名
}
