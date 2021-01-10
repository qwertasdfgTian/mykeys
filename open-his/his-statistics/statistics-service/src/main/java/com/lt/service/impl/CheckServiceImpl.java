package com.lt.service.impl;

import com.lt.domain.Check;
import com.lt.domain.CheckStat;
import com.lt.dto.CheckQueryDto;
import com.lt.mapper.CheckMapper;
import com.lt.service.CheckService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: Mr.Tian
 */
@Service
public class CheckServiceImpl implements CheckService {


    @Autowired
    private CheckMapper checkMapper;

    @Override
    public List<Check> queryCheck(CheckQueryDto checkQueryDto) {
        return this.checkMapper.queryCheck(checkQueryDto);
    }

    @Override
    public List<CheckStat> queryCheckStat(CheckQueryDto checkQueryDto) {
        return this.checkMapper.queryCheckStat(checkQueryDto);
    }
}
