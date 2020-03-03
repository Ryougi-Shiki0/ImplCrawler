package com.myproject.multifunctioncrawler.service;

import com.myproject.multifunctioncrawler.pojo.OrderInfo;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.vo.GoodsVo;

public interface RushService {
    public OrderInfo rush(User user, GoodsVo goods);
}
