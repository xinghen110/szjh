package com.magustek.szjh.basedataset.entity.vo;

import com.magustek.szjh.basedataset.entity.IEPlanDimenValueSet;
import com.magustek.szjh.utils.KeyValueBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class IEPlanDimenValueSetVO extends IEPlanDimenValueSet {
    private List<KeyValueBean> dmList;
}
