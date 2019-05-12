package com.magustek.szjh.basedataset.entity.vo;

import com.magustek.szjh.configset.service.ConfigDataSourceSetService;
import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RollPlanHeaderVO extends RollPlanHeadDataArchive {
    private String busta;//业务状态（01-未支付、02-已支付）
    private String butxt;//业务状态描述
    private String ztype;//款项（01-预付款、02-进度款）
    private String zptxt;//款项
    private List<RollPlanItemVO> itemVOS;
    private ConfigDataSourceSetService config;

    public void setBusta(String busta){
        this.busta = busta;
        if(config!=null){
            butxt = config.getDescByQcgrpAndQcode("SFST", busta);
        }
    }

    public void setZtype(String ztype){
        this.ztype = ztype;
        if(config!=null){
            zptxt = config.getDescByQcgrpAndQcode("SFKU", ztype);
        }
    }
}
