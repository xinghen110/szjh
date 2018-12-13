package com.magustek.szjh.basedataset.entity;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 计划业务表：合同收付款条款
 *
 * */
@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "version")})
public class IEPlanTermsSet extends BaseEntity{
    @Column(length = 20) private String htnum;          //合同管理编号
    @Column(length = 4) private String fenum;           //行编号
    @Column(length = 1) private String sfpla;           //单一字符标识
    @Column(length = 4) private String sfkxz;           //收付款性质
    @Column(length = 6) private String sfkbl;           //比例
    @Column private BigDecimal sfkje;                   //收付款金额
    @Column private String sfktj;                       //收付款条件
    @Column(length = 1) private String msgtype;         //消息类型
    @Column(length = 220) private String msgtext;       //消息文本
    @Column(length = 4) private String aflag;           //动作标识
    @Column(length = 30) private String version;        //明细版本（储存编制日期或计划编号）

}
