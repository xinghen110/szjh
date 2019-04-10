package com.magustek.szjh.utils.base;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.Transient;

@Getter
@Setter
public class BasePage extends BaseFilter{
    @Transient protected String page;
    @Transient protected String size;

    public Pageable getPageRequest(){
        if(Strings.isNullOrEmpty(page)){
            page = "0";
        }
        if(Strings.isNullOrEmpty(size)){
            size = "10";
        }
        return new PageRequest(StringToInt(page),StringToInt(size));
    }

    private int StringToInt(String s){
        return Integer.parseInt(s);
    }

}
