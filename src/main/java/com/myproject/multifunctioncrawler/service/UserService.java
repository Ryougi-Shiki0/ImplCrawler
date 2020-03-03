package com.myproject.multifunctioncrawler.service;

import com.myproject.multifunctioncrawler.dao.UserDao;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.result.CodeMsg;
import com.myproject.multifunctioncrawler.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;


public interface UserService {
    String COOKIE_NAME_TOKEN = "token";

     User getById(long id);
    boolean login(HttpServletResponse response,LoginVo loginVo);
    User getByToken(HttpServletResponse response, String token);

}
