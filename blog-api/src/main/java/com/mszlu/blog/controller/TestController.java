package com.mszlu.blog.controller;

import com.mszlu.blog.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test(){
        return Result.success(null);
    }
}
