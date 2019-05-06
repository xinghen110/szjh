package com.magustek.szjh.utils.base;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.Transient;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BasePage extends BaseFilter{
    @Transient protected String page;
    @Transient protected String size;

    public Pageable initPageRequest(){
        if(Strings.isNullOrEmpty(page)){
            page = "0";
        }
        if(Strings.isNullOrEmpty(size)){
            size = "10";
        }
        return new PageRequest(StringToInt(page),StringToInt(size));
    }

    public Page initPageImpl(List list){
        Pageable req = this.initPageRequest();
        return new PageImpl(list.subList(req.getOffset(),req.getOffset()+req.getPageSize()>list.size()?list.size():req.getOffset()+req.getPageSize()), req, list.size());
    }

    private int StringToInt(String s){
        return Integer.parseInt(s);
    }

}
