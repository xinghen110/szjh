package com.magustek.szjh.plan.utils;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WearsType {

    private BigDecimal budget = new BigDecimal("0.00");          //预算款
    private BigDecimal progress = new BigDecimal("0.00");        //进度款
    private BigDecimal settlement = new BigDecimal("0.00");      //结算款
    private BigDecimal warranty = new BigDecimal("0.00");        //质保金
}
