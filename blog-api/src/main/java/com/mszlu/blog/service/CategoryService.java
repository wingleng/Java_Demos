package com.mszlu.blog.service;

import com.mszlu.blog.vo.CategoryVo;

import java.util.List;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);
}
