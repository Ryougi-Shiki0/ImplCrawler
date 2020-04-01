package com.myproject.multifunctioncrawler.service;

import com.myproject.multifunctioncrawler.pojo.OrderInfo;
import com.myproject.multifunctioncrawler.pojo.RushOrder;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.vo.GoodsVo;

public interface OrderService {

    RushOrder getRushOrderByUserIdGoodsId(long userId, long goodsId);

    OrderInfo createOrder(User user, GoodsVo goods);

    void deleteOrders();

    OrderInfo getOrderById(long orderId);
}
