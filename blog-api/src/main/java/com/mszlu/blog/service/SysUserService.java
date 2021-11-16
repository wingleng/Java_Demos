package com.mszlu.blog.service;

import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.vo.Result;

public interface SysUserService {
    /**
     * 根据用户id查找用户
     * @param id
     * @return
     */
    SysUser findUserById(Long id);

    /**
     * 用来验证用户账户密码是否正确
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password);

    /**
     * 通过token来获取用户的详细信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 通过用户名进行查找
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);
}
