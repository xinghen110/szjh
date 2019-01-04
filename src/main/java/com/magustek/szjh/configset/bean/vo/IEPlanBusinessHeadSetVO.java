package com.magustek.szjh.configset.bean.vo;

import com.magustek.szjh.basedataset.entity.IEPlanSelectValueSet;
import com.magustek.szjh.configset.bean.IEPlanBusinessHeadSet;
import com.magustek.szjh.configset.service.ConfigDataSourceSetService;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.groovy.GroovyBean;
import com.magustek.szjh.utils.groovy.GroovyUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Setter
@Component
public class IEPlanBusinessHeadSetVO extends IEPlanBusinessHeadSet {

    private String butxt;//业务状态（01-未支付、02-已支付）
    private String ztext;//款项（01-预付款、02-进度款）

    private final String buSource = "SFST";
    private final String zSource = "SFKU";

    //private List<IEPlanBusinessHeadLineVO> headLineVOList;
    private List<IEPlanBusinessItemSetVO> itemVOList;


    private ConfigDataSourceSetService dataSourceSetService;

    public IEPlanBusinessHeadSetVO(ConfigDataSourceSetService dataSourceSetService) {
        this.dataSourceSetService = dataSourceSetService;
    }

    public void setBusta(String busta) {
        super.setBusta(busta);
        //获取描述
        this.butxt = dataSourceSetService.getDescByQcgrpAndQcode(buSource,busta);
    }

    public void setZtype(String ztype) {
        super.setZtype(ztype);
        //获取描述
        this.butxt = dataSourceSetService.getDescByQcgrpAndQcode(zSource,ztype);
    }

    //根据取数进行计算，判断是否显示
    public boolean calc(List<IEPlanSelectValueSet> valueList){
        //获取变量列表
        Set<String> vars = new HashSet<>(Arrays.asList(super.getVariv().split(",")));
        if(ClassUtils.isEmpty(vars)){
            return false;
        }
        Map<String, Object> binding = new HashMap<>();
        //取出所有待计算数据
        valueList.forEach(v->{
            if(vars.contains(v.getSdart())){
                binding.put(v.getSdart(), v.getSdval());
            }
        });
        GroovyBean bean = new GroovyBean();
        bean.setBinding(binding);
        bean.setCommand(super.getCondi());
        GroovyUtils groovyUtils = new GroovyUtils();

        //返回是否显示的计算结果
        return Boolean.valueOf((String)groovyUtils.exec(bean));
    }
}
