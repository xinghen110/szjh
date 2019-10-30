package com.magustek.szjh.plan.service.impl;

import com.magustek.szjh.basedataset.entity.IEPlanDimenValueSet;
import com.magustek.szjh.basedataset.entity.RollPlanHeadData;
import com.magustek.szjh.basedataset.entity.RollPlanItemData;
import com.magustek.szjh.basedataset.service.IEPlanDimenValueSetService;
import com.magustek.szjh.basedataset.service.RollPlanDataService;
import com.magustek.szjh.configset.bean.IEPlanBusinessItemSet;
import com.magustek.szjh.configset.service.IEPlanBusinessItemSetService;
import com.magustek.szjh.plan.bean.PlanHeader;
import com.magustek.szjh.plan.bean.RollPlanHeadDataArchive;
import com.magustek.szjh.plan.bean.RollPlanItemDataArchive;
import com.magustek.szjh.plan.bean.vo.RollPlanHeadDataArchiveVO;
import com.magustek.szjh.plan.bean.vo.RollPlanItemDataArchiveVO;
import com.magustek.szjh.plan.dao.RollPlanHeadDataArchiveDAO;
import com.magustek.szjh.plan.dao.RollPlanItemDataArchiveDAO;
import com.magustek.szjh.plan.service.RollPlanArchiveService;
import com.magustek.szjh.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("RollPlanArchiveService")
public class RollPlanArchiveServiceImpl implements RollPlanArchiveService {
    private RollPlanHeadDataArchiveDAO rollPlanHeadDataArchiveDAO;
    private RollPlanItemDataArchiveDAO rollPlanItemDataArchiveDAO;
    private RollPlanDataService rollPlanDataService;
    private IEPlanDimenValueSetService iePlanDimenValueSetService;
    private IEPlanBusinessItemSetService iePlanBusinessItemSetService;

    public RollPlanArchiveServiceImpl(RollPlanHeadDataArchiveDAO rollPlanHeadDataArchiveDAO, RollPlanItemDataArchiveDAO rollPlanItemDataArchiveDAO, RollPlanDataService rollPlanDataService, IEPlanDimenValueSetService iePlanDimenValueSetService, IEPlanBusinessItemSetService iePlanBusinessItemSetService) {
        this.rollPlanHeadDataArchiveDAO = rollPlanHeadDataArchiveDAO;
        this.rollPlanItemDataArchiveDAO = rollPlanItemDataArchiveDAO;
        this.rollPlanDataService = rollPlanDataService;
        this.iePlanDimenValueSetService = iePlanDimenValueSetService;
        this.iePlanBusinessItemSetService = iePlanBusinessItemSetService;
    }

    @Override
    public void copyData(PlanHeader planHeader) throws Exception {
        //合同基础数据表相应字段打上标记，不允许删除。
        //int count = iePlanSelectValueSetService.updateReferencedByVersion("X",planHeader.getCkdate());
        //log.warn("将版本为【{}】的合同基础数据打上引用标记，不允许删除！标记数据行数{}。", planHeader.getCkdate(), count);
        //每个合同的维度数据
        List<IEPlanDimenValueSet> dimenList = iePlanDimenValueSetService.getAllByVersion(planHeader.getCkdate());
        if(ClassUtils.isEmpty(dimenList)){
            throw new Exception("无维度数据！ckdate:"+planHeader.getCkdate());
        }
        Map<String, List<IEPlanDimenValueSet>> dmvalMapByHtsno = dimenList
                .stream()
                .collect(Collectors.groupingBy(IEPlanDimenValueSet::getHtsno));
        log.warn("取出维度数据，行数{}。", dimenList.size());

        //滚动计划抬头数据
        List<RollPlanHeadData> rollPlanHeadDataList = rollPlanDataService.getAllByVersion(planHeader.getCkdate(), planHeader.getBukrs());
        log.warn("取出滚动计划抬头数据，行数{}。", rollPlanHeadDataList.size());

        if(ClassUtils.isEmpty(rollPlanHeadDataList)){
            throw new Exception("无可复制的数据！ckdate:"+planHeader.getCkdate()+ ",bukrs:" +planHeader.getBukrs());
            //return;
        }

        List<RollPlanHeadDataArchive> rollPlanHeadDataArchiveList = new ArrayList<>();

        //滚动计划行项目数据
        List<RollPlanItemData> rollPlanItemDataList = rollPlanDataService.getAllByHead(rollPlanHeadDataList);

        log.warn("取出滚动计划行项目数据，行数{}。", rollPlanItemDataList.size());
        List<RollPlanItemDataArchive> rollPlanItemDataArchiveList = new ArrayList<>(rollPlanItemDataList.size());

        //复制抬头数据
        rollPlanHeadDataList.forEach(head->{
            RollPlanHeadDataArchive headArchive = new RollPlanHeadDataArchive();
            BeanUtils.copyProperties(head, headArchive);
            headArchive.setId(null);
            headArchive.setRollId(head.getId());
            headArchive.setPlanHeadId(planHeader.getId());

            //获取合同的维度数据，格式-D100:6010,D110:50003521,D120:SHIHAO1,
            if(!ClassUtils.isEmpty(dmvalMapByHtsno.get(head.getHtsno()))){
                Map<String, List<IEPlanDimenValueSet>> dmvalMapByDmart = dmvalMapByHtsno.get(head.getHtsno()).stream().collect(Collectors.groupingBy(IEPlanDimenValueSet::getDmart));
                StringBuilder builder = new StringBuilder();
                dmvalMapByDmart.forEach((k, v)->
                        builder.append(k).append(":").append(v.get(0).getDmval()).append(","));
                headArchive.setDmval(builder.toString());
            }

            rollPlanHeadDataArchiveList.add(headArchive);
        });
        log.warn("滚动计划抬头数据计算完毕，复制行数{}。", rollPlanHeadDataArchiveList.size());

        //复制行项目数据
        rollPlanItemDataList.forEach(item->{
            RollPlanItemDataArchive itemArchive = new RollPlanItemDataArchive();
            BeanUtils.copyProperties(item, itemArchive);
            itemArchive.setId(null);
            itemArchive.setHeadId(item.getHeadId().getId());
            itemArchive.setPlanHeadId(planHeader.getId());//计划抬头ID（使用父类的-headId与headArchive关联）
            rollPlanItemDataArchiveList.add(itemArchive);
        });
        log.warn("滚动计划行项目数据数据计算完毕，复制行数{}。", rollPlanItemDataArchiveList.size());
        rollPlanHeadDataArchiveDAO.save(rollPlanHeadDataArchiveList);
        log.warn("滚动计划抬头数据保存完毕，复制行数{}。", rollPlanHeadDataArchiveList.size());
        int i = rollPlanItemDataArchiveDAO.copyRollPlanItemByVersionAndPlanHeadId(planHeader.getCkdate(), planHeader.getId());
        log.warn("滚动计划行项目数据数据保存完毕，复制行数{}。", i);
    }

    @Transactional
    @Override
    public void deleteData(PlanHeader header) {
        //查找出待删除数据
        //List<RollPlanHeadDataArchive> headList = rollPlanHeadDataArchiveDAO.findAllByPlanHeadId(header.getId());
        //List<RollPlanItemDataArchive> itemList = rollPlanItemDataArchiveDAO.findAllByPlanHeadId(header.getId());
        //删除数据
        rollPlanHeadDataArchiveDAO.deleteAllByPlanHeadId(header.getId());
        rollPlanItemDataArchiveDAO.deleteAllByPlanHeadId(header.getId());
        //合同基础数据表相应字段删除引用标记，该数据将允许删除。
        //int count = iePlanSelectValueSetService.updateReferencedByVersion(null,header.getCkdate());
        //log.warn("将版本为【{}】的合同基础数据取消引用标记，将允许删除！标记数据行数{}。", header.getCkdate(), count);
    }

    @Override
    public List<RollPlanHeadDataArchive> getHeadDataArchiveList(Long PlanHeadId) {
        return rollPlanHeadDataArchiveDAO.findAllByPlanHeadId(PlanHeadId);
    }

    @Override
    public List<RollPlanHeadDataArchive> getHeadData(String zbart, String dmval, String dtval, Long planHeadId, boolean firstMonth, boolean lastMonth) {
        LocalDate localDate = ClassUtils.StringToLocalDateWithoutException(dtval+"01");
        Assert.notNull(localDate, "dtval 格式错误："+dtval);
        //月计划第一个月需要包含之前所有数据
        if(firstMonth){
            LocalDate lastday = localDate.with(TemporalAdjusters.lastDayOfMonth());
            return rollPlanHeadDataArchiveDAO.findAllByPlanHeadIdAndDtvalLessThanEqualAndDmvalContainsAndZbart(planHeadId,
                    lastday.toString().replace("-",""),
                    "D110:"+dmval,
                    zbart);
        }
        //月计划最后一个月，需要包含之后所有数据
        if(lastMonth){
            LocalDate firstday = localDate.with(TemporalAdjusters.lastDayOfMonth());
            return rollPlanHeadDataArchiveDAO.findAllByPlanHeadIdAndDtvalGreaterThanAndDmvalContainsAndZbart(planHeadId,
                    firstday.toString().replace("-", ""),
                    "D110:"+dmval,
                    zbart);
        }
        //返回当月所有数据
        return rollPlanHeadDataArchiveDAO.findAllByPlanHeadIdAndDtvalContainsAndDmvalContainsAndZbart(planHeadId, dtval, "D110:"+dmval, zbart);
    }

    @Override
    public List<RollPlanHeadDataArchive> getHeadDataByPlanHeadIdAndDmvalAndZbart(String zbart, String dmval, Long planHeadId) {
        return rollPlanHeadDataArchiveDAO.findAllByPlanHeadIdAndDmvalContainsAndZbart(planHeadId, dmval, zbart);
    }

    @Override
    public List<RollPlanHeadDataArchive> getHeadDataByPlanHeadIdAndZbart(Long planHeadId, String zbart) {
        return rollPlanHeadDataArchiveDAO.findAllByPlanHeadIdAndZbart(planHeadId, zbart);
    }

    @Override
    public List<RollPlanItemDataArchive> getItemDataByHeadIdAndImnum(List<Long> headIdList, List<String> imnumList) {
        return rollPlanItemDataArchiveDAO.findAllByHeadIdInAndImnumIn(headIdList, imnumList);
    }

    @Override
    public List<RollPlanItemDataArchive> getItemDataByPlanHeadId(Long PlanHeadId) {
        return rollPlanItemDataArchiveDAO.findAllByPlanHeadId(PlanHeadId);
    }

    @Transactional
    @Override
    @Modifying
    public void saveItemList(List<RollPlanItemDataArchive> changedList) {
        Iterable<RollPlanItemDataArchive> save = rollPlanItemDataArchiveDAO.save(changedList);
        save.forEach(s-> log.warn("id:{}, dtval:{}",s.getId(),s.getDtval()));
    }

    @Override
    public void saveHeadList(List<RollPlanHeadDataArchive> changedList) {
        Iterable<RollPlanHeadDataArchive> save = rollPlanHeadDataArchiveDAO.save(changedList);
        save.forEach(s-> log.warn("id:{}, dtval:{}",s.getId(),s.getDtval()));
    }

    @Override
    public List<RollPlanItemDataArchiveVO> getItemListByPlanHeaderIdAndStartEndDate(Long id, String start, String end) {
        Map<String, List<IEPlanBusinessItemSet>> businessItemMap = iePlanBusinessItemSetService.getAll().stream().collect(Collectors.groupingBy(IEPlanBusinessItemSet::getImnum));
        return RollPlanItemDataArchiveVO.cover(rollPlanItemDataArchiveDAO.findAllByPlanHeadIdAndCtdtpAndDtvalBetween(id, start, end),businessItemMap);
    }

    @Cacheable(value = "RollPlanHeadDataArchiveVOList")
    @Override
    public List<RollPlanHeadDataArchiveVO> getListByPlanHeaderId(Long id) {
        return RollPlanHeadDataArchiveVO.cover(rollPlanHeadDataArchiveDAO.findHeadWithItemByPlanHeadId(id));
    }
}
