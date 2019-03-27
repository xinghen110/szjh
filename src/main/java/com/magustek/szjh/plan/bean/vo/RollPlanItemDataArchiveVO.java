package com.magustek.szjh.plan.bean.vo;

import com.magustek.szjh.configset.bean.IEPlanBusinessItemSet;
import com.magustek.szjh.configset.service.IEPlanBusinessItemSetService;
import com.magustek.szjh.plan.bean.RollPlanItemDataArchive;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 待处理合同列表bean，用于接收数据库数据
 *
 * @author hexin*/
@ApiModel(value = "待处理合同列表bean，用于接收数据库数据")
@Getter
@Setter
public class RollPlanItemDataArchiveVO extends RollPlanItemDataArchive implements Serializable {
    private Long planHeadId;
    private Long rollId;        //滚动计划抬头ID
    private String dmval;       //合同的维度数据，格式-D100:6010,D110:50003521,D120:SHIHAO1,


    private String bukrs;       //公司代码
    private String htsno;       //合同流水号
    private String htnum;       //合同管理编号
    private String hdnum;       //抬头编号
    private String zbart;       //经营指标分类
    private BigDecimal wears;   //金额
    private String imtxt;       //环节编号描述
    //private String dtval;       //第一个计划日期(yyyyMMdd)
    //private String stval;       //合同条款
    //private String version;     //明细版本（储存编制日期或计划编号）


    public static RollPlanItemDataArchiveVO cover(Object[] o, Map<String, List<IEPlanBusinessItemSet>> businessItemMap){
        RollPlanItemDataArchiveVO vo = new RollPlanItemDataArchiveVO();
        //head.htsno, head.htnum, head.stval, head.wears, head.dmval, head.bukrs, head.hdnum, head.zbart, head.version
        //item.caval, item.dtval, item.imnum, item.odue, item.sdart
        vo.setHtsno((String)o[0]);
        vo.setHtnum((String)o[1]);
        vo.setWears((BigDecimal)o[3]);
        vo.setDmval((String)o[4]);
        vo.setBukrs((String)o[5]);
        vo.setHdnum((String)o[6]);
        vo.setZbart((String)o[7]);
        vo.setCaval((Integer)o[9]);
        vo.setDtval((String)o[10]);
        vo.setImnum((String)o[11]);
        vo.setImtxt(businessItemMap.get(vo.getImnum()).get(0).getImtxt());
        vo.setOdue((String)o[12]);
        vo.setSdart((String)o[13]);
        return vo;
    }

    public static List<RollPlanItemDataArchiveVO> cover(List<Object[]> list, Map<String, List<IEPlanBusinessItemSet>> businessItemMap){
        List<RollPlanItemDataArchiveVO> voList = new ArrayList<>();
        list.forEach(o->voList.add(cover(o, businessItemMap)));
        return voList;
    }
}
