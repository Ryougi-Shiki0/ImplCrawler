package com.myproject.multifunctioncrawler.service;

import com.myproject.multifunctioncrawler.pojo.OrderInfo;
import com.myproject.multifunctioncrawler.pojo.RushOrder;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.vo.GoodsVo;

public interface OrderService {
    public RushOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);
    public OrderInfo createOrder(User user, GoodsVo goods);
}
