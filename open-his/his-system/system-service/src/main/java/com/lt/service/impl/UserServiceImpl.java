package com.lt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.constants.Constants;
import com.lt.dto.UserDto;
import com.lt.mapper.RoleMapper;
import com.lt.utils.AppMd5Utils;
import com.lt.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.domain.User;
import com.lt.mapper.UserMapper;
import com.lt.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    //查询电话号码
    @Override
    public User queryUserByPhone(String phone) {
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq(User.COL_PHONE,phone);
        User user = userMapper.selectOne(qw);
        return user;
    }

    @Override
    public User getOne(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public DataGridView listUserForPage(UserDto userDto) {
        Page<User> page=new Page<>(userDto.getPageNum(), userDto.getPageSize());
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(userDto.getUserName()), User.COL_USER_NAME, userDto.getUserName());
        qw.like(StringUtils.isNotBlank(userDto.getPhone()), User.COL_PHONE, userDto.getPhone());
        qw.eq(StringUtils.isNotBlank(userDto.getStatus()), User.COL_STATUS, userDto.getStatus());
        qw.eq(userDto.getDeptId()!=null, User.COL_DEPT_ID, userDto.getDeptId());

        qw.ge(null!= userDto.getBeginTime(), User.COL_CREATE_TIME, userDto.getBeginTime());
        qw.le(null!= userDto.getEndTime(), User.COL_CREATE_TIME, userDto.getEndTime());
        qw.orderByAsc(User.COL_USER_ID);
        this.userMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public int addUser(UserDto userDto) {
        User user=new User();
        BeanUtil.copyProperties(userDto,user);
        user.setUserType(Constants.USER_NORMAL);//用户类型是系统用户
        String defaultPwd=user.getPhone().substring(5);
        user.setCreateBy(userDto.getSimpleUser().getUserName());
        user.setCreateTime(DateUtil.date());
        user.setSalt(AppMd5Utils.createSalt());
        user.setPassword(AppMd5Utils.md5(defaultPwd,user.getSalt(),2));
        return this.userMapper.insert(user);
    }

    @Override
    public int updateUser(UserDto userDto) {
        User user=this.userMapper.selectById(userDto.getUserId());
        if(null==user){
            return 0;
        }
        BeanUtil.copyProperties(userDto,user);
        //设置修改人
        user.setUpdateBy(userDto.getSimpleUser().getUserName());
        return this.userMapper.updateById(user);
    }

    @Override
    public int deleteUserByIds(Long[] userIds) {
        List<Long> ids = Arrays.asList(userIds);
        //根据用户ids删除sys_role_user里面的数据
        this.roleMapper.deleteRoleUserByUserIds(ids);
        return this.userMapper.deleteBatchIds(ids);
    }

    @Override
    public void resetPassWord(Long[] userIds) {
        for (Long userId : userIds) {
            User user=this.userMapper.selectById(userId);
            String defaultPwd="";
            //如果是超级 管理员
            if (user.getUserType().equals(Constants.USER_ADMIN)){
                defaultPwd="123456";
            }else{
                defaultPwd=user.getPhone().substring(5);
            }
            user.setSalt(AppMd5Utils.createSalt());
            user.setPassword(AppMd5Utils.md5(defaultPwd,user.getSalt(),2));
            this.userMapper.updateById(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq(User.COL_STATUS, Constants.STATUS_TRUE);
        qw.eq(User.COL_USER_TYPE,Constants.USER_NORMAL);
        qw.orderByAsc(User.COL_USER_ID);
        return this.userMapper.selectList(qw);
    }

    @Override
    public List<User> queryUsersNeedScheduling(Long userId,Long deptId) {
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq(deptId!=null,User.COL_DEPT_ID,deptId);
        qw.eq(userId!=null,User.COL_USER_ID,userId);
        qw.eq(User.COL_SCHEDULING_FLAG,Constants.SCHEDULING_FLAG_TRUE);//需要排班的
        qw.eq(User.COL_STATUS,Constants.STATUS_TRUE);//可用的用户
        return this.userMapper.selectList(qw);
    }
}


