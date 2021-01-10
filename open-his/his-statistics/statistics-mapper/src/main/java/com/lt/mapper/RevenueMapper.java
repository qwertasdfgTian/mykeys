package com.lt.mapper;

import com.lt.domain.Income;
import com.lt.domain.Refund;
import com.lt.dto.RevenueQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Mr.Tian
 */
public interface RevenueMapper {
    /**
     * 查询收入的数据
     *
     * @param revenueQueryDto
     * @return
     */
    List<Income> queryIncome(@Param("revenue") RevenueQueryDto revenueQueryDto);

    /**
     * 查询退费的数据
     *
     * @param revenueQueryDto
     * @return
     */
    List<Refund> queryRefund(@Param("revenue") RevenueQueryDto revenueQueryDto);
}
