package com.lt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.constants.Constants;
import com.lt.domain.DictData;
import com.lt.dto.DictTypeDto;
import com.lt.mapper.DictDataMapper;
import com.lt.service.DictTypeService;
import com.lt.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import com.lt.domain.DictType;
import com.lt.mapper.DictTypeMapper;

@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Autowired
    DictTypeMapper dictTypeMapper;

    @Autowired
    private DictDataMapper dictDataMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public DataGridView listPage(DictTypeDto dictTypeDto) {

        Page<DictType> page = new Page<>(dictTypeDto.getPageNum(), dictTypeDto.getPageSize());
        QueryWrapper<DictType> qw = new QueryWrapper<>();
        qw.eq(StringUtils.isNotBlank(dictTypeDto.getStatus()), DictType.COL_STATUS, dictTypeDto.getStatus());
        qw.like(StringUtils.isNotBlank(dictTypeDto.getDictName()), DictType.COL_DICT_NAME, dictTypeDto.getDictName());
        qw.like(StringUtils.isNotBlank(dictTypeDto.getDictType()), DictType.COL_DICT_TYPE, dictTypeDto.getDictType());
        qw.le(null != dictTypeDto.getBeginTime(), DictType.COL_CREATE_TIME, dictTypeDto.getBeginTime());
        qw.ge(null != dictTypeDto.getEndTime(), DictType.COL_CREATE_TIME, dictTypeDto.getEndTime());
        dictTypeMapper.selectPage(page, qw);
        //数据条数和数据本身
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public DataGridView list() {
        QueryWrapper<DictType> qw = new QueryWrapper<>();
        //只查可用的
        qw.eq(DictType.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null, this.dictTypeMapper.selectList(qw));
    }

    @Override
    public Boolean checkDictTypeUnique(Long dictId, String dictType) {
        dictId = (dictId == null) ? -1L : dictId;
        QueryWrapper<DictType> qw = new QueryWrapper<>();
        qw.eq(DictType.COL_DICT_TYPE, dictType);
        DictType sysDictType = this.dictTypeMapper.selectOne(qw);
        if (null != sysDictType && dictId.longValue() != sysDictType.getDictId().longValue()) {
            return true; //说明存在
        }
        return false; //说明不存在
    }

    @Override
    public int insert(DictTypeDto dictTypeDto) {
        DictType dictType = new DictType();
        //把dictTypeDto拷贝到dictType
        BeanUtil.copyProperties(dictTypeDto, dictType);
        dictType.setCreateTime(DateUtil.date());
        dictType.setCreateBy(dictTypeDto.getSimpleUser().getUserName());
        return this.dictTypeMapper.insert(dictType);
    }

    @Override
    public int update(DictTypeDto dictTypeDto) {
        DictType dictType = new DictType();
        BeanUtil.copyProperties(dictTypeDto, dictType);
        dictType.setUpdateBy(dictTypeDto.getSimpleUser().getUserName());
        return this.dictTypeMapper.updateById(dictType);
    }

    @Override
    public int deleteDictTypeByIds(Long[] dictIds) {
        List<Long> ids = Arrays.asList(dictIds);
        if (null != ids && ids.size() > 0) {
            return this.dictTypeMapper.deleteBatchIds(ids);
        } else {
            return -1;
        }
    }

    @Override
    public DictType selectDictTypeById(Long dictId) {
        return this.dictTypeMapper.selectById(dictId);
    }

    /**
     * 同步缓存的做法
     * 1，查询出所有可用的字典类型数据
     * 2，再根据字典的类型查询字典数据
     * 3，把字典数据生成json存到redis
     * 设计key
     * dict:dictType
     * 如dict:sys_user_sex --->[{},{},{}]
     *
     */
    @Override
    public void dictCacheAsync() {
        //查询出所有可用的字典类型数据
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        qw.eq(DictType.COL_STATUS,Constants.STATUS_TRUE);
        List<DictType> dictTypes = this.dictTypeMapper.selectList(qw);
        for (DictType dictType : dictTypes) {
            QueryWrapper<DictData> qdw=new QueryWrapper<>();
            qdw.eq(DictData.COL_STATUS,Constants.STATUS_TRUE);
            qdw.eq(DictData.COL_DICT_TYPE,dictType.getDictType());
            qdw.orderByAsc(DictData.COL_DICT_SORT);
            List<DictData> dictDataList = dictDataMapper.selectList(qdw);
            //转成json串
            String json= JSON.toJSONString(dictDataList);
            ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
            opsForValue.set(Constants.DICT_REDIS_PROFIX+dictType.getDictType(),json);
        }
    }
}

