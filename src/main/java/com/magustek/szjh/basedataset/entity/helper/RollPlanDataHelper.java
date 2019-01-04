package com.magustek.szjh.basedataset.entity.helper;

import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import com.magustek.szjh.basedataset.entity.RollPlanItemData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RollPlanDataHelper {
    private RollPlanHeadData headData;
    private List<RollPlanItemData> itemList;
}
