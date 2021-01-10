package com.lt.service.impl;

import com.lt.domain.Workload;
import com.lt.domain.WorkloadStat;
import com.lt.dto.WorkloadQueryDto;
import com.lt.mapper.WorkloadMapper;
import com.lt.service.WorkloadService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: Mr.Tian
 */
@Service
public class WorkloadServiceImpl implements WorkloadService {


    @Autowired
    private WorkloadMapper workloadMapper;

    @Override
    public List<Workload> queryWorkload(WorkloadQueryDto workloadQueryDto) {
        return this.workloadMapper.queryWorkload(workloadQueryDto);
    }

    @Override
    public List<WorkloadStat> queryWorkloadStat(WorkloadQueryDto workloadQueryDto) {
        return this.workloadMapper.queryWorkloadStat(workloadQueryDto);
    }
}
