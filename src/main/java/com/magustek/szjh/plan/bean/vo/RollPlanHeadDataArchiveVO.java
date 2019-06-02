package com.magustek.szjh.plan.bean.vo;

import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import com.magustek.szjh.utils.ClassUtils;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 计划表：合同滚动计划抬头数据（每个计划一版）
 * */
@ApiModel(value = "计划相关-合同滚动计划抬头数据")
@Getter
@Setter
public class RollPlanHeadDataArchiveVO extends RollPlanHeadDataArchive {

    private String caart;   //业务计算指标
    private String dmart;   //维度指标
    private Integer caval;   //历史能力值

    transient private List<RollPlanItemDataArchiveVO> itemList;

    public static List<RollPlanHeadDataArchiveVO> cover(List<Object[]> objectList){
        List<RollPlanHeadDataArchiveVO> voList = new ArrayList<>();
        Map<Object, List<Object[]>> voMap = objectList.stream().collect(Collectors.groupingBy(o -> o[0])); //根据head-id分组
        voMap.forEach((id,list)->{
            if(ClassUtils.isEmpty(list)){
                return;
            }
            //合同抬头数据
            RollPlanHeadDataArchiveVO head = new RollPlanHeadDataArchiveVO();
            voList.add(head);
            Object[] o = list.get(0);
            head.setId(((BigInteger) o[0]).longValue());
            head.setBukrs((String) o[1]);
            head.setHtsno((String) o[2]);
            head.setHtnum((String) o[3]);
            head.setHdnum((String) o[4]);
            head.setZbart((String) o[5]);
            head.setWears((BigDecimal) o[6]);
            head.setDtval((String) o[7]);
            head.setStval((String) o[8]);
            head.setVersion((String) o[9]);
            head.setDmval((String) o[10]);
            head.setRollId(((BigInteger) o[11]).longValue());
            head.itemList = new ArrayList<>(list.size());

            //行项目数据
            list.forEach(objects->{
                RollPlanItemDataArchiveVO item = new RollPlanItemDataArchiveVO();
                item.setId(((BigInteger)objects[12]).longValue());
                item.setHeadId(((BigInteger)objects[13]).longValue());
                item.setImnum((String)objects[14]);
                item.setDtval((String)objects[15]);
                item.setStval((String)objects[16]);
                item.setCtdtp((String)objects[17]);
                item.setSdart((String)objects[18]);
                item.setOdue((String)objects[19]);
                item.setCaval((Integer) objects[20]);
                head.itemList.add(item);
            });
        });
        return voList;
    }
}
