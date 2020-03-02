package com.myproject.multifunctioncrawler.service.Impl;

import com.myproject.multifunctioncrawler.dao.UserDao;
import com.myproject.multifunctioncrawler.exception.GlobalException;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.result.CodeMsg;
import com.myproject.multifunctioncrawler.service.UserService;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import com.myproject.multifunctioncrawler.service.redis.UserKey;
import com.myproject.multifunctioncrawler.utils.MD5Util;
import com.myproject.multifunctioncrawler.utils.UUIDUtil;
import com.myproject.multifunctioncrawler.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private UserDao userDao;

    @Autowired
    RedisService redisService;

    @Override
    public User getById(long id) {
        return userDao.getById(id);
    }

    @Override
    public boolean login(HttpServletResponse response,LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile=loginVo.getMobile();
        String formPass=loginVo.getPassword();
        User user=getById(Long.parseLong(mobile));
        if(user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass=user.getPassword();
        String saltDB=user.getSalt();
        String calcPass=MD5Util.formPassToDBPass(formPass,saltDB);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token= UUIDUtil.uuid();
        redisService.set(UserKey.token,token,user);
        Cookie cookie=new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
        return true;
    }
}
