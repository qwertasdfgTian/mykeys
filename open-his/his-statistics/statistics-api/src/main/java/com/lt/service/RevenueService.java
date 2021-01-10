package com.lt.service;

import com.lt.dto.RevenueQueryDto;

import java.util.Map;

/**
 * @Author: Mr.Tian
 */
public interface RevenueService {
    /**
     * 查询收支统计的数据
     * @param revenueQueryDto
     * @return
     */
    Map<String, Object> queryAllRevenueData(RevenueQueryDto revenueQueryDto);
}
