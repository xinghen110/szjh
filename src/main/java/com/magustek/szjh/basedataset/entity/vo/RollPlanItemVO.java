package com.magustek.szjh.basedataset.entity.vo;

import com.magustek.szjh.basedataset.entity.RollPlanItemData;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.constant.IEPlanSelectDataConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.time.LocalDate;

@Setter
@Getter
@Slf4j
public class RollPlanItemVO extends RollPlanItemData {
    private String value;
    private String vtype;//值类型

    public String getValue() {
        if(IEPlanSelectDataConstant.RESULT_TYPE_CHAR.equalsIgnoreCase(vtype)){
            //类型为【CHAR】时，值取【stval】字段
            value = super.getStval();
        }else{
            //值取【stval】字段
            LocalDate localDate = null;
            try {
                localDate = ClassUtils.StringToLocalDate(super.getDtval());
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
            value = localDate==null?"":localDate.toString();
        }
        return value;
    }
}
