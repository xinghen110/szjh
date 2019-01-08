package com.magustek.szjh.configset.bean.vo;

import com.magustek.szjh.configset.bean.IEPlanBusinessItemSet;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class IEPlanBusinessItemSetVO extends IEPlanBusinessItemSet {

    //private String sdtypValue;//取数类型（G-取值，C-计算）
    private String sdartValue;//取数指标
    private Integer caartValue;//到上一环节历史能力值
    private String ctbgnValue;//合同约定起始环节
    private Integer ctdatValue;//合同约定期限
    private String ctdtpValue;//约定期限日期类型（W-工作日/N-自然日）
    private BigDecimal sdcutValue;//对应金额指标（金额）
    private String maxHtnum;//最新合同管理编号
}
