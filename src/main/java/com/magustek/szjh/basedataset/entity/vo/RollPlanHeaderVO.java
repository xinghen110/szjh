package com.magustek.szjh.basedataset.entity.vo;

import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RollPlanHeaderVO extends RollPlanHeadData {
    private String busta;//业务状态（01-未支付、02-已支付）
    private String ztype;//款项（01-预付款、02-进度款）
    private List<RollPlanItemVO> itemVOS;

    public String getBusta(){
        if("01".equals(busta)){
            return "未支付";
        }
        if("02".equals(busta)){
            return "已支付";
        }
        return "";
    }

    public String getZtype(){
        if("01".equals(ztype)){
            return "预付款";
        }
        if("02".equals(ztype)){
            return "进度款";
        }
        return "";
    }
}
