package com.mszlu.blog.cache;


import java.lang.annotation.*;

/**
 * 定义缓存的切点
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    //过期时间
    long expire() default 1 * 60 * 1000;
    //定义缓存标识
    String name() default "";

}