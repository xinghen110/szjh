package com.magustek.szjh.report.bean;

import com.google.common.base.Strings;
import com.magustek.szjh.utils.base.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 报表bean
 * */
@ApiModel(value = "统计报表-报表bean")
@Getter
@Setter
public class Report extends BasePage {
    @ApiModelProperty(value = "版本，格式：yyyy-MM-dd")
    private String version;

    public String getVersion() {
        return Strings.isNullOrEmpty(version)? LocalDate.now().toString():version;
    }
}
