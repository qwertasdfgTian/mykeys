package com.lt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lt.constants.Constants;
import com.lt.domain.CareHistory;
import com.lt.domain.CareOrder;
import com.lt.domain.CareOrderItem;
import com.lt.domain.Registration;
import com.lt.dto.CareHistoryDto;
import com.lt.dto.CareOrderDto;
import com.lt.dto.CareOrderFormDto;
import com.lt.dto.CareOrderItemDto;
import com.lt.mapper.CareHistoryMapper;
import com.lt.mapper.CareOrderItemMapper;
import com.lt.mapper.CareOrderMapper;
import com.lt.mapper.RegistrationMapper;
import com.lt.service.CareService;
import com.lt.utils.IdGeneratorSnowflake;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Mr.Tian
 */
@Service
public class CareServiceImpl implements CareService {

    @Autowired
    private CareHistoryMapper careHistoryMapper;

    @Autowired
    private CareOrderMapper careOrderMapper;

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    @Autowired
    private RegistrationMapper registrationMapper;


    @Override
    public List<CareHistory> queryCareHistoryByPatientId(String patientId) {
        QueryWrapper<CareHistory> qw=new QueryWrapper<>();
        qw.eq(CareHistory.COL_PATIENT_ID,patientId);
        return this.careHistoryMapper.selectList(qw);
    }

    @Override
    public CareHistory saveOrUpdateCareHistory(CareHistoryDto careHistoryDto) {
        CareHistory careHistory=new CareHistory();
        BeanUtil.copyProperties(careHistoryDto,careHistory);
        if (StringUtils.isNotBlank(careHistory.getChId())) {//做修改
            this.careHistoryMapper.updateById(careHistory);
        }else{//做添加
            careHistory.setChId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_CH));
            this.careHistoryMapper.insert(careHistory);
        }
        return careHistory;
    }

    @Override
    public CareHistory queryCareHistoryByRegId(String regId) {
        QueryWrapper<CareHistory> qw=new QueryWrapper<>();
        qw.eq(CareHistory.COL_REG_ID,regId);
        return this.careHistoryMapper.selectOne(qw);
    }

    @Override
    public List<CareOrder> queryCareOrdersByChId(String chId) {
        QueryWrapper<CareOrder> qw=new QueryWrapper<>();
        qw.eq(CareOrder.COL_CH_ID,chId);
        return this.careOrderMapper.selectList(qw);
    }

    @Override
    public List<CareOrderItem> queryCareOrderItemsByCoId(String coId) {
        QueryWrapper<CareOrderItem> qw=new QueryWrapper<>();
        qw.eq(CareOrderItem.COL_CO_ID,coId);
        return this.careOrderItemMapper.selectList(qw);
    }

    @Override
    public CareHistory queryCareHistoryByChId(String chId) {
        return this.careHistoryMapper.selectById(chId);
    }

    @Override
    public int saveCareOrderItem(CareOrderFormDto careOrderFormDto) {
        CareOrderDto careOrderDto = careOrderFormDto.getCareOrder();
        CareOrder careOrder=new CareOrder();
        BeanUtil.copyProperties(careOrderDto,careOrder);
        careOrder.setCreateBy(careOrderDto.getSimpleUser().getUserName());
        careOrder.setCreateTime(DateUtil.date());
        int i=this.careOrderMapper.insert(careOrder);//保存处方主表
        List<CareOrderItemDto> careOrderItems = careOrderFormDto.getCareOrderItems();
        //保存详情数据
        for (CareOrderItemDto careOrderItemDto : careOrderItems) {
            CareOrderItem careOrderItem=new CareOrderItem();
            BeanUtil.copyProperties(careOrderItemDto,careOrderItem);
            careOrderItem.setCoId(careOrder.getCoId());
            careOrderItem.setCreateTime(DateUtil.date());
            careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_0);//未支付
            //一定要记得加哦，泽新还没有加
            careOrderItem.setItemId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_ITEM));
            this.careOrderItemMapper.insert(careOrderItem);
        }
        return i;
    }

    @Override
    public CareOrderItem queryCareOrderItemByItemId(String itemId) {
        return this.careOrderItemMapper.selectById(itemId);
    }


    @Override
    public int deleteCareOrderItemByItemId(String itemId) {
        //注意点，如果删除了，要更新careOrder主表的all_amount
        CareOrderItem careOrderItem=this.careOrderItemMapper.selectById(itemId);
        String coId=careOrderItem.getCoId();//取出主表ID
        //删除
        int i=this.careOrderItemMapper.deleteById(itemId);

        //再根据coID查询还存在的详情数据
        QueryWrapper<CareOrderItem> qw=new QueryWrapper<>();
        qw.eq(CareOrderItem.COL_CO_ID,coId);
        List<CareOrderItem> careOrderItems=this.careOrderItemMapper.selectList(qw);
        if(careOrderItems!=null&&careOrderItems.size()>0){
            //重新计算总价格
            BigDecimal allAmount=new BigDecimal("0");
            for (CareOrderItem orderItem : careOrderItems) {
                allAmount=allAmount.add(orderItem.getAmount());
            }
            //再根据coId查询主表的数据
            CareOrder careOrder=this.careOrderMapper.selectById(coId);
            //更新主表的数据
            careOrder.setAllAmount(allAmount);
            this.careOrderMapper.updateById(careOrder);
        }else{
            //说明没有详情了，直接干掉主表里面的数据
            this.careOrderMapper.deleteById(coId);
        }
        return i;
    }

    @Override
    public int visitComplete(String regId) {
        Registration registration=new Registration();
        registration.setRegistrationId(regId);
        registration.setRegistrationStatus(Constants.REG_STATUS_3);
        return this.registrationMapper.updateById(registration);
    }
}
