package com.mszlu.blog.dao.dos;

import lombok.Data;

//这个数据是用在文章归档上的
@Data
public class Archives {

    private Integer year;

    private Integer month;

    private Long count;
}
