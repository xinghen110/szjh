package com.magustek.szjh.utils.base;

import com.google.common.base.Strings;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.Transient;

public class BasePage {
    @Transient protected String page;
    @Transient protected String size;

    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

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
