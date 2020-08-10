package com.lt.blog.service.impl;

import com.lt.blog.mapper.CarouselMapper;
import com.lt.blog.mapper.MusicMapper;
import com.lt.blog.pojo.Carousel;
import com.lt.blog.service.CarouselService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * </p>
 */
@Service
@Slf4j
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper CarouselMapper;


    @Override
    public void save(Carousel carousel) {
        CarouselMapper.save(carousel);
    }

    @Override
    public void update(Carousel carousel) {
        CarouselMapper.update(carousel);
    }

    @Override
    public void deleteById(Integer id) {
        CarouselMapper.deleteById(id);
    }

    @Override
    public List<Carousel> findAll() {
        return CarouselMapper.findAll();
    }

    @Override
    public Carousel get(Integer id) {
        return CarouselMapper.get(id);
    }
}
