package com.lt.service;

import com.lt.domain.Check;
import com.lt.domain.CheckStat;
import com.lt.dto.CheckQueryDto;

import java.util.List;

/**
 * @Author: Mr.Tian
 */
public interface CheckService {
    /**
     * 查询检查项列表
     * @param checkQueryDto
     * @return
     */
    List<Check> queryCheck(CheckQueryDto checkQueryDto);

    /**
     * 查询检查项统计列表
     * @param checkQueryDto
     * @return
     */
    List<CheckStat> queryCheckStat(CheckQueryDto checkQueryDto);
}
