package com.magustek.szjh.configset.bean;

import com.magustek.szjh.utils.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置表：滚动计划-抬头配置
 * */
@Getter
@Setter
@Entity
public class IEPlanBusinessItemSet extends BaseEntity{
    public static final transient String CALC = "1";    //计算出来的值（计划值）
    public static final transient String GET = "0";     //取出来的值（实际值）
    @Column(nullable = false, length = 10) private String hdnum;//抬头编号
    @Column(nullable = false, length = 10) private String imnum;//项目编号
    @Column(nullable = false, length = 10) private String nimnu;//下一个项目编号
    @Column(length = 4) private String sdtyp;//取数类型（G-取值，C-计算）
    @Column(length = 4) private String sdart;//取数指标
    @Column(length = 4) private String caart;//到下一环节历史能力值
    @Column(length = 4) private String ctbgn;//合同约定起始环节
    @Column(length = 4) private String ctdat;//合同约定期限
    @Column(length = 4) private String ctdtp;//约定期限日期类型（W-工作日/N-自然日）
    @Column(length = 4) private String sdcur;//对应金额指标（金额）
    @Column(length = 10) private String vtype;//值类型
    @Column(length = 1) private String hjbgn;//第一环节
    @Column(length = 1) private String hjend;//最后环节
    @Column(length = 1)  private String msgtype;//消息类型
    @Column(length = 220)private String msgtext;//消息文本
    //@Column(length = 4) private String bukrs;//公司代码
    //@Column(length = 4) private String rptyp;//报表类型
}
