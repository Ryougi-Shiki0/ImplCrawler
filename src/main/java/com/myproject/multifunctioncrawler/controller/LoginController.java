package com.myproject.multifunctioncrawler.controller;

import com.myproject.multifunctioncrawler.result.CodeMsg;
import com.myproject.multifunctioncrawler.result.Result;
import com.myproject.multifunctioncrawler.service.ImageInfoService;
import com.myproject.multifunctioncrawler.service.UserService;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import com.myproject.multifunctioncrawler.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Arthas
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        String token = userService.login(response, loginVo);
        return Result.success(token);
    }
}