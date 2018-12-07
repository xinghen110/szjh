package com.magustek.szjh.configset.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * 配置表：数据源配置
 * */
@Getter
@Setter
@Entity
public class ConfigDataSourceSet extends BaseEntity{

    @JSONField(name = "Qcgrp") private String qcgrp; //代码组
    @JSONField(name = "Qcode") private String qcode; //代码
    @JSONField(name = "Srflg") private String srflg; //是否包含子类型
    @JSONField(name = "Cotxt") private String cotxt; //短文本
    @JSONField(name = "Htart") private String htart; //业务类
    @JSONField(name = "Zcflx") private String zcflx; //盖章文本对应类型
    @JSONField(name = "Msgtype") private String msgtype; //消息类型
    @JSONField(name = "Msgtext") private String msgtext; //消息文本
    @JSONField(name = "Aflag") private String aflag; //动作标识
    @JSONField(name = "Loginname") private String loginname; //登录用户名
    @JSONField(name = "Usersource") private String usersource; //用户来源
    @JSONField(name = "Orgcode") private String orgcode; //组织机构编号
    @JSONField(name = "Deptcode") private String deptcode; //部门编号
    @JSONField(name = "Actorcode") private String actorcode; //所属岗位

}
