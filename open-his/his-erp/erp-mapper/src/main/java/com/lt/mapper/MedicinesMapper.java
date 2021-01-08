package com.lt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.domain.Medicines;
import org.apache.ibatis.annotations.Param;

public interface MedicinesMapper extends BaseMapper<Medicines> {
    int deductionMedicinesStorage(@Param("medicinesId") Long medicinesId, @Param("num") long num);
}