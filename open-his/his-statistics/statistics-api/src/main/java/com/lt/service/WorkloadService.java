package com.lt.service;

import com.lt.domain.Workload;
import com.lt.domain.WorkloadStat;
import com.lt.dto.WorkloadQueryDto;

import java.util.List;

/**
 * @Author: Mr.Tian
 */
public interface WorkloadService {
    /**
     * 医生工作量统计列表
     * @param workloadQueryDto
     * @return
     */
    List<Workload> queryWorkload(WorkloadQueryDto workloadQueryDto);

    /**
     * 总体工作量统计列表
     * @param workloadQueryDto
     * @return
     */
    List<WorkloadStat> queryWorkloadStat(WorkloadQueryDto workloadQueryDto);
}
