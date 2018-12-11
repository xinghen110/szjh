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
 * 计划业务表：合同付款构成明细
 *
 * */
@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "version")})
public class IEPlanPaymentSet extends BaseEntity {
    @Column(length = 20) private String htnum;      //合同管理编号
    @Column(length = 4) private String fenum;       //行编号
    @Column(length = 14) private String htsno;      //合同流水号
    @Column(length = 4) private String ztype;       //类型
    @Column(length = 4) private String ztyp1;       //子类型
    @Column(length = 5) private String waers;       //货币
    @Column(length = 15) private String wtext;      //短文本
    @Column private BigDecimal zzbtr;               //带符号的金额
    @Column(length = 60) private String znote;      //备注
    @Column(length = 4) private String zjflg;       //增减标识
    @Column(length = 4) private String bukrs;       //公司代码
    @Column(length = 10) private String belnr;      //凭证编号
    @Column(length = 4) private String gjahr;       //财政年度
    @Column(length = 3) private String buzei;       //行
    @Column(length = 17) private String htbnr;      //合同编号
    @Column private BigDecimal slbfb;               //税率
    @Column private BigDecimal sjbtr;               //税金
    @Column private BigDecimal fpbtr;               //发票金额
    @Column(length = 24) private String erpfp;      //发票
    @Column private BigDecimal zzbtr1;              //收/付款币种金额
    @Column(length = 5) private String wares1;      //货币
    @Column private BigDecimal zukur;               //汇率
    @Column private BigDecimal yfsyj;               //剩余金额
    @Column(length = 1) private String msgtype;     //消息类型
    @Column(length = 220) private String msgtext;   //消息文本
    @Column(length = 4) private String aflag;       //动作标识
    @Column(length = 30) private String version;    //明细版本（储存编制日期或计划编号）
}
