package com.mszlu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mszlu.blog.dao.dos.Archives;
import com.mszlu.blog.dao.mapper.ArticleMapper;
import com.mszlu.blog.dao.pojo.Article;
import com.mszlu.blog.service.ArticleService;
import com.mszlu.blog.service.SysUserService;
import com.mszlu.blog.service.TagService;
import com.mszlu.blog.vo.ArticleVo;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.PageParams;
import com.sun.crypto.provider.ARCFOURCipher;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 首页返回文章列表
     * @param pageParams
     * @return
     */

    @Override
    public Result listArticle(PageParams pageParams) {
        /**
         * 1. 分页查询article数据库表
         */
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWapper = new LambdaQueryWrapper<>();

        //是否置顶进行排序
        queryWapper.orderByDesc(Article::getWeight,Article::getCreateDate);

        Page<Article> articlePage = articleMapper.selectPage(page,queryWapper);
        List<Article> records = articlePage.getRecords();

        //转成vo对象，貌似说是不能直接操作对象，所以要生成一个新的对象
        List<ArticleVo> articleVoList = copyList(records,true,true);

        return Result.success(articleVoList);
    }

    /**
     * 返回最热文章
     * @param limits
     * @return
     */
    @Override
    public Result hotArticle(int limits) {
        LambdaQueryWrapper<Article> queryWapper = new LambdaQueryWrapper<>();
        queryWapper.orderByDesc(Article::getViewCounts);
        queryWapper.select(Article::getId,Article::getTitle);
        queryWapper.last("limit "+limits);

        List<Article> articles = articleMapper.selectList(queryWapper);
        return Result.success(copyList(articles,false,false));
    }

    /**
     * 返回最新文章
     * @param limits
     * @return
     */
    @Override
    public Result newArticle(int limits) {
        LambdaQueryWrapper<Article> queryWapper = new LambdaQueryWrapper<>();
        queryWapper.orderByDesc(Article::getCreateDate);
        queryWapper.select(Article::getId,Article::getTitle);
        queryWapper.last("limit "+limits);
        //select id,title from article order by create_date desc desc limit 5
        List<Article> articles = articleMapper.selectList(queryWapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record :records){
            articleVoList.add(copy(record,isTag,isAuthor));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor){
        ArticleVo articleVo = new ArticleVo();
//        使用BeanUtils中的方法，将属性复制到新的对象当中去
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
//并不是所有借口都需要标签，作者信息，所以这里做一个判断处理
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        //这个同上
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }

        return articleVo;
    }

}
