package com.lt.controller.system;

import com.lt.aspectj.annotation.Log;
import com.lt.aspectj.enums.BusinessType;
import com.lt.dto.DictTypeDto;
import com.lt.service.DictTypeService;
import com.lt.utils.ShiroSecurityUtils;
import com.lt.vo.AjaxResult;
import com.lt.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Program: open-his
 * @Description:
 * @Author: Mr.Tian
 * @Create: 2020-11-15 12:51
 **/
@RestController
@RequestMapping("system/dict/type")
public class DictTypeController {

    @Autowired
    private DictTypeService dictTypeService;

    @GetMapping("listForPage")
    public AjaxResult listForPage(DictTypeDto dictTypeDto){
        DataGridView dataGridView = this.dictTypeService.listPage(dictTypeDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加
     */
    @PostMapping("addDictType")
    @Log(title = "添加字典类型",businessType = BusinessType.INSERT)
    public AjaxResult addDictType(@Validated DictTypeDto dictTypeDto) {
        if (dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(), dictTypeDto.getDictType())) {
            return AjaxResult.fail("新增字典【" + dictTypeDto.getDictName() + "】失败，字典类型已存在");
        }
        dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictTypeService.insert(dictTypeDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateDictType")
    @Log(title = "修改字典类型",businessType = BusinessType.UPDATE)
    public AjaxResult updateDictType(@Validated DictTypeDto dictTypeDto) {
        if (dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(), dictTypeDto.getDictType())) {
            return AjaxResult.fail("修改字典【" + dictTypeDto.getDictName() + "】失败，字典类型已存在");
        }
        dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictTypeService.update(dictTypeDto));
    }


    /**
     * 根据ID查询一个字典信息
     */
    @GetMapping("getOne/{dictId}")
    public AjaxResult getDictType(@PathVariable @Validated @NotNull(message = "字典ID不能为空") Long dictId) {
        return AjaxResult.success(this.dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteDictTypeByIds/{dictIds}")
    @Log(title = "删除字典类型",businessType = BusinessType.DELETE)
    public AjaxResult updateDictType(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] dictIds) {
        return AjaxResult.toAjax(this.dictTypeService.deleteDictTypeByIds(dictIds));
    }

    /**
     * 查询所有可用的字典类型
     */
    @GetMapping("selectAllDictType")
    public AjaxResult selectAllDictType(){
        return AjaxResult.success(this.dictTypeService.list().getData());
    }

    /**
     * 同步字典数据到redis
     */
    @GetMapping("dictCacheAsync")
    @Log(title = "同步字典数据到redis",businessType = BusinessType.OTHER)
    public AjaxResult dictCacheAsync(){
        try {
            this.dictTypeService.dictCacheAsync();
            return AjaxResult.success();
        }catch (Exception e){
            System.out.println(e);
            return AjaxResult.error();
        }
    }

}
