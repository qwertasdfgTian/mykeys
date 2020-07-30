package com.lt.blog.controller;

import com.lt.blog.exception.BlogException;
import com.lt.blog.utils.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping(value = "/exception/{id}",method = RequestMethod.GET)
    public Result<Object> test(@PathVariable Integer id){
        if(id==1){
            return new Result<>();
        }else{
            throw new BlogException("发生了异常");
        }

    }
}
