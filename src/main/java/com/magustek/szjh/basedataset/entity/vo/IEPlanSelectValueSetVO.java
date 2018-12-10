package com.magustek.szjh.basedataset.entity.vo;

import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.utils.KeyValueBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IEPlanSelectValueSetVO extends IEPlanSelectValueSet {
    private List<KeyValueBean> sdList;
}
