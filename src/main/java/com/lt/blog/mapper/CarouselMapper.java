package com.lt.blog.mapper;

import com.lt.blog.pojo.Carousel;
import com.lt.blog.pojo.Music;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 接口访问日志表Mapper
 * </p>
 *
 */
@Component
public interface CarouselMapper {

    /**
     * 添加
     */
    void save(Carousel carousel);

    /**
     * 修改
     */
    void update(Carousel carousel);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(Integer id);

    List<Carousel> findAll();

    Carousel get(Integer id);
}
