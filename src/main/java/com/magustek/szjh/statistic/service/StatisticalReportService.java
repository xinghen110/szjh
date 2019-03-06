package com.magustek.szjh.statistic.service;

import com.magustek.szjh.statistic.bean.Report;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface StatisticalReportService {
    Page<Map<String, String>> getOutputTaxDetailByVersion(Report report);
}
