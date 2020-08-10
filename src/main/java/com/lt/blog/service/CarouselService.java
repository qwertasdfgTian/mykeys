package com.lt.blog.service;

import com.lt.blog.pojo.Carousel;
import com.lt.blog.pojo.Music;
import com.lt.blog.utils.Page;

import java.util.List;

/**
 * <p>
 * </p>
 */
public interface CarouselService {

    /**
     * 保存
     */
    void save(Carousel carousel);

    /**
     * 更新
     */
    void update(Carousel carousel);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(Integer id);

    //全查询
    List<Carousel> findAll();

     Carousel get(Integer id);
}
