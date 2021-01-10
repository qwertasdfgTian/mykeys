package com.lt.service;

import com.lt.domain.Drug;
import com.lt.domain.DrugStat;
import com.lt.dto.DrugQueryDto;

import java.util.List;

/**
 * @Author: Mr.Tian
 */
public interface DrugService {
    /**
     * 查询发药统计列表
     * @param drugQueryDto
     * @return
     */
    List<Drug> queryDrug(DrugQueryDto drugQueryDto);

    /**
     * 查询发药数量统计列表
     * @param drugQueryDto
     * @return
     */
    List<DrugStat> queryDrugStat(DrugQueryDto drugQueryDto);
}
