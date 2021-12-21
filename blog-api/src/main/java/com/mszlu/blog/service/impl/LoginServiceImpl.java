package com.mszlu.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.mszlu.blog.dao.pojo.SysUser;
import com.mszlu.blog.service.LoginService;
import com.mszlu.blog.service.SysUserService;
import com.mszlu.blog.utils.JWTUtils;
import com.mszlu.blog.vo.ErrorCode;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.LoginParam;
import io.netty.util.internal.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    private static final String salt = "mszlu!@#";//加密盐

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String>  redisTemplate;

    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1. 检查参数是否合法
         * 2. 根据用户名和密码去user表中查询，是否存在
         * 3. 如果不存在 登录失败
         * 4. 如果存在  使用jwt  生成token  返回给前端
         * 5. token放进redis中  redis  token：user信息  设置过期时间
         * （登录认证的时候，先认证token字符串是否合法，再去redis中）认证是否存在
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();

        //判断是否为空
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        password = DigestUtils.md5Hex(password + salt);
        //到数据库中查找
        SysUser sysUser = sysUserService.findUser(account,password);
        if(sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }


        //创建token，并且将当前用户放进redis中。。。。
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);



        //最后登录成功，将token信息返回。
        return Result.success(token);
    }

    /**
     * 使用token来获取当前用户
     * 1. 首先验证这个token是否有效
     * 2. 然后再redis中获取token
     * 3. 返回。
     * @param token
     * @return
     */

    @Override
    public SysUser checkToken(String token) {
        //首先判断这个token是否为空：
        if(StringUtils.isBlank(token)){
            return null;
        }
        //2.进行验证，一些什么验证之类的，但是这里没必要，能够证明
        Map<String,Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap == null){
            return null;
        }
        //3.最后到redis中进行查询，看是否有缓存到redis中
        String userJson = redisTemplate.opsForValue().get("TOKEN_"+token);
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson,SysUser.class);
        return sysUser;
    }


    @Override
    public Result logout(String token) {
        //退出登录，后端做不了什么，就将redis删除了就好了
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 注册步骤
         * 1. 首先判断参数是否合法
         * 2. 其次判断数据库中是否已经存在
         * 3. 不存在，注册用户
         * 4. 生成token
         * 5. 放进redis中
         * 6. 所有操作都要放进事务中，一旦出现任何差错，就回滚
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if(StringUtils.isBlank(account)
        || StringUtils.isBlank(password)
        || StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg()) ;
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if(sysUser!=null){
            return Result.fail(ErrorCode.ACCOUNT_EXITS.getCode(),ErrorCode.ACCOUNT_EXITS.getMsg());
        }
        //注册用户
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1);
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);
        //创建token，并且放进redis中
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);

        return Result.success(token);
    }
}
