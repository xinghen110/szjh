package com.magustek.szjh.utils.base;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.Transient;
import java.util.List;

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

    public Page getPageImpl(List list){
        Pageable req = this.getPageRequest();
        return new PageImpl(list.subList(req.getOffset(),req.getOffset()+req.getPageSize()>list.size()?list.size():req.getOffset()+req.getPageSize()), req, list.size());
    }

    private int StringToInt(String s){
        return Integer.parseInt(s);
    }

}
