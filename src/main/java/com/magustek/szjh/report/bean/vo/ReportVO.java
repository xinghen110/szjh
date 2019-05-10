package com.magustek.szjh.report.bean.vo;

import com.google.common.base.Strings;
import com.magustek.szjh.utils.base.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * 报表bean
 * */
@ApiModel(value = "统计报表-报表bean")
@Getter
@Setter
public class ReportVO extends BasePage {
    @ApiModelProperty(value = "版本，格式：yyyy-MM-dd")
    private String version;
    private Long id;
    private String caart;
    private List<String> caartList;
    private String dpnum;
    private String zbart;


    public String getVersion() {
        return Strings.isNullOrEmpty(version)? LocalDate.now().toString():version;
    }
}
