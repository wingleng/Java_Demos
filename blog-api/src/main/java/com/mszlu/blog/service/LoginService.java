package com.mszlu.blog.service;

import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LoginService {

    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    /**
     * 用来校验token的方法
     * @param token
     */
    SysUser checkToken(String token);

    /**
     * 用来退出登录的方法
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册功能的实现
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);

}
