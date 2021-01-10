package com.lt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据id查询该节点有没有子节点
     *
     * @Param: menuId
     */
    Long queryChildCountByMenuId(@Param("menuId") Long menuId);

    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户id查询用户拥有的菜单信息
     *
     * @param userId
     * @return
     */
    List<Menu> selectMenuListByUserId(@Param("userId") Serializable userId);
}