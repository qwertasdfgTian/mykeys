package com.lt.blog.service.impl;

import com.lt.blog.enums.ResultEnum;
import com.lt.blog.enums.StateEnums;
import com.lt.blog.exception.BlogException;
import com.lt.blog.mapper.AboutMapper;
import com.lt.blog.pojo.About;
import com.lt.blog.service.AboutService;
import com.lt.blog.utils.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * </p>
 */
@Service
@Slf4j
public class AboutServiceImpl implements AboutService {

    @Autowired
    private AboutMapper aboutMapper;

    @Override
    public void save(About about) {
        aboutMapper.save(about);
    }

    @Override
    public void update(About about) {
        aboutMapper.updateById(about);
    }

    @Override
    public About getById(Integer id) {
        return aboutMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public About read() {
        About about = aboutMapper.getAbout();
        if (about == null) {
            return null;
        }
        // 更新阅读数
        about.setAboutRead(about.getAboutRead() + 1);
        aboutMapper.updateById(about);
        return about;
    }

    @Override
    public void deleteById(Integer id) {
        aboutMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableById(Integer id) {
        // 查询是否已经存在启用的关于我的
        About about = aboutMapper.getAbout();
        if (about != null) {
            throw new BlogException(ResultEnum.ERROR.getCode(), "当前已存在启用中的关于我的");
        }
        about = aboutMapper.getById(id);
        about.setEnable(StateEnums.ENABLED.getCode());
        aboutMapper.updateEnable(about);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableById(Integer id) {
        About about = aboutMapper.getById(id);
        about.setEnable(StateEnums.NOT_ENABLE.getCode());
        aboutMapper.updateEnable(about);
    }

    @Override
    public Page<About> getByPage(Page<About> page) {
        // 查询数据
        List<About> aboutList = aboutMapper.getByPage(page);
        page.setList(aboutList);
        // 查询总数
        int totalCount = aboutMapper.getCountByPage(page);
        page.setTotalCount(totalCount);
        return page;
    }
}
