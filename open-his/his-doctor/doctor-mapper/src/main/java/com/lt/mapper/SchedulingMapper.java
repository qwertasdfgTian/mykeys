package com.lt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.domain.Scheduling;

import java.util.List;

public interface SchedulingMapper extends BaseMapper<Scheduling> {
    List<Long> queryHasSchedulingDeptIds(Long deptId, String schedulingDay, String schedulingType, String subsectionType);
}