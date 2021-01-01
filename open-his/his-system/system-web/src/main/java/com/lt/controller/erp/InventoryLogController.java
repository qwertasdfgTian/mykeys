package com.lt.controller.erp;

import com.lt.aspectj.annotation.Log;
import com.lt.aspectj.enums.BusinessType;
import com.lt.controller.BaseController;
import com.lt.domain.InventoryLog;
import com.lt.dto.InventoryLogDto;
import com.lt.dto.MedicinesDto;
import com.lt.service.InventoryLogService;
import com.lt.service.MedicinesService;
import com.lt.utils.ShiroSecurityUtils;
import com.lt.vo.AjaxResult;
import com.lt.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Auther: Mr.Tian
 * @Description:入库记录控制器
 */
@RestController
@RequestMapping("erp/inventoryLog")
public class InventoryLogController extends BaseController {


    @Reference//使用dubbo的引用
    private InventoryLogService inventoryLogService;

    /**
     * 分页查询
     */
    @GetMapping("listInventoryLogForPage")
    @HystrixCommand
    public AjaxResult listMedicinesForPage(InventoryLogDto inventoryLogDto){
        DataGridView gridView = this.inventoryLogService.listInventoryLogPage(inventoryLogDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }

}
