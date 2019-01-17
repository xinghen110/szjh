package com.magustek.szjh.configset.bean.vo;

import com.magustek.szjh.configset.bean.IEPlanScreenHeadSet;
import com.magustek.szjh.configset.bean.IEPlanScreenItemSet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class IEPlanScreenVO extends IEPlanScreenHeadSet {
    private List<IEPlanScreenItemSet> itemSetList;
}
