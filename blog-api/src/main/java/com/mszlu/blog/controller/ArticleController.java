package com.mszlu.blog.controller;



import com.mszlu.blog.dao.pojo.Article;
import com.mszlu.blog.service.ArticleService;
//import com.mszlu.blog.vo.Archive;
import com.mszlu.blog.vo.ArticleVo;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mszlu.blog.service.ArticleService;
//都是进行json数据交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams){

        return articleService.listArticle(pageParams);
    }

    /**
     * 首页最热文章
     * @return
     */
    @PostMapping("hot")
    public Result hotArticle(){
        int limits = 5;
        return articleService.hotArticle(limits);
    }


    /**
     * 首页最热文章
     * @return
     */
    @PostMapping("new")
    public Result newArticle(){
        int limits = 5;
        return articleService.newArticle(limits);
    }


    /**
     * 首页  文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }


}
