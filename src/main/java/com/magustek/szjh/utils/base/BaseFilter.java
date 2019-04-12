package com.magustek.szjh.utils.base;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.magustek.szjh.utils.ClassUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Transient;
import java.util.*;

@Getter
@Setter
@Slf4j
public class BaseFilter {
    @Transient private FilterParam filterParam;

    public List<Map<String, String>> filter(List<Map<String, String>> list){
        long l = System.currentTimeMillis();
        if(filterParam == null || filterParam.filterData==null || ClassUtils.isEmpty(filterParam.filterData.domains)){
            return list;
        }
        List<Map<String, String>> resultList = new ArrayList<>();
        Arrays.asList(filterParam.filterData.domains).forEach(domain -> {
            ArrayList<Map<String, String>> domainList = new ArrayList<>();
            list.forEach(map->{
                if(!ClassUtils.isEmpty(domain.role) && domain.role.length >= 2){
                    String value = map.get(domain.role[0]);
                    boolean flag = !Strings.isNullOrEmpty(value) && !ClassUtils.isEmpty(domain.value) && !Strings.isNullOrEmpty(domain.value[0]);
                    switch (domain.role[1]){
                        // string 处理
                        case "equal":
                            if(flag && value.toLowerCase().equals(domain.value[0].toLowerCase())){
                                domainList.add(map);
                            }
                            break;
                        case "notEqual":
                            if(flag && !value.toLowerCase().equals(domain.value[0].toLowerCase())){
                                domainList.add(map);
                            }
                            break;
                        case "contain":
                            if(flag && value.toLowerCase().contains(domain.value[0].toLowerCase())){
                                domainList.add(map);
                            }
                            break;
                        case "notContain":
                            if(flag && !value.toLowerCase().contains(domain.value[0].toLowerCase())){
                                domainList.add(map);
                            }
                            break;

                        // date 处理
                        case "isNull":
                            if(Strings.isNullOrEmpty(value)){
                                domainList.add(map);
                            }
                            break;
                        case "before":
                            if(flag && value.compareTo(domain.value[0])<=0){
                                domainList.add(map);
                            }
                            break;
                        case "after":
                            if(flag && value.compareTo(domain.value[0])>=0){
                                domainList.add(map);
                            }
                            break;
                        case "between":
                            if(flag && domain.value.length>=2
                                    && !Strings.isNullOrEmpty(domain.value[1])
                                    && value.compareTo(domain.value[0])>=0
                                    && value.compareTo(domain.value[1])<=0){
                                domainList.add(map);
                            }
                            break;
                        // number 处理
                        case "=":
                            if(flag && value.toLowerCase().equals(domain.value[0].toLowerCase())){
                                domainList.add(map);
                            }
                            break;
                        case ">=":
                            if(flag && value.toLowerCase().compareTo(domain.value[0].toLowerCase())>=0){
                                domainList.add(map);
                            }
                            break;
                        case "<=":
                            if(flag && value.toLowerCase().compareTo(domain.value[0].toLowerCase())<=0){
                                domainList.add(map);
                            }
                            break;
                    }
                }
            });
            if("and".equals(filterParam.role)){
                List<Map<String, String>> tempList = new ArrayList<>();
                if(ClassUtils.isEmpty(resultList)) {
                    resultList.addAll(domainList);
                }else{
                    resultList.forEach(resultMap->{
                        for(Map<String, String> domainMap : domainList){
                            if(JSON.toJSONString(resultMap).equals(JSON.toJSONString(domainMap))){
                                tempList.add(resultMap);
                                break;
                            }
                        }
                    });
                    resultList.clear();
                    resultList.addAll(tempList);
                }
            }else{
                resultList.addAll(domainList);
            }
        });
        log.warn("列表条件过滤耗时{}秒", (System.currentTimeMillis()-l) / 1000.00);
        return resultList;
    }
}
@Setter
@Getter
class Domain{
    @Transient String[] role;
    @Transient String[] value;
}
@Setter
@Getter
class FilterData{
    @Transient Domain[] domains;
}
@Setter
@Getter
class FilterParam{
    @Transient FilterData filterData;
    @Transient String role;
}


/*
* {
	"filterData": {
		"domains": [
			{
				"role": [
					"G418",
					"equal"
				],
				"value": [
					"56565"
				]
			},
			{
				"role": [
					"G418",
					"contain"
				],
				"value": [
					"34343"
				]
			},
			{
				"role": [
					"G434",
					"between"
				],
				"value": [
					"2019-03-31",
					"2019-04-08"
				]
			},
			{
				"role": [
					"G432",
					"isNull"
				],
				"value": []
			}
		]
	},
	"role": "and",
	"visible": false
}
* */