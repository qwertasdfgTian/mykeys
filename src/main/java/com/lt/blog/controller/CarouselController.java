package com.lt.blog.controller;

import com.lt.blog.pojo.Carousel;
import com.lt.blog.pojo.Music;
import com.lt.blog.service.CarouselService;
import com.lt.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService CarouselService;

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<Object> save(@RequestBody Carousel carousel) {
        CarouselService.save(carousel);
        return new Result<>("添加成功！");
    }

    /**
     * 更新
     *
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<Object> update(@RequestBody Carousel carousel) {
        CarouselService.update(carousel);
        return new Result<>("修改成功！");
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public Result<Object> delete(@PathVariable Integer id) {
        CarouselService.deleteById(id);
        return new Result<>("删除成功！");
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Object> findAll() {
        return new Result<>(CarouselService.findAll());
    }
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Result<Object> get(@PathVariable Integer id) {
        return new Result<>(CarouselService.get(id));
    }

}
