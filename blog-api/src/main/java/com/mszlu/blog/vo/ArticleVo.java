package com.mszlu.blog.vo;

//import com.mszlu.blog.dao.pojo.ArticleBody;
//import com.mszlu.blog.dao.pojo.Category;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.dao.pojo.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;

    private String summary;

//    private int commentCounts;
    private Integer commentCounts;

//    private int viewCounts;
    private Integer viewCounts;

    private int weight;
    /**
     * 创建时间
     */
    private String createDate;

    private String author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo categorys;

}

