package com.mszlu.blog.service;

import com.mszlu.blog.dao.pojo.CommentParam;
import com.mszlu.blog.vo.Result;

public interface CommentsService {
    Result commentsByArticleId(Long articleId);

    Result comment(CommentParam commentParam);
}
