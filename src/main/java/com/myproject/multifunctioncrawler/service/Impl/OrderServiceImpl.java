package com.myproject.multifunctioncrawler.service.Impl;

import com.myproject.multifunctioncrawler.dao.OrderDao;
import com.myproject.multifunctioncrawler.pojo.OrderInfo;
import com.myproject.multifunctioncrawler.pojo.RushOrder;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.service.OrderService;
import com.myproject.multifunctioncrawler.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	OrderDao orderDao;
	
	public RushOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
		return orderDao.getRushOrderByUserIdGoodsId(userId, goodsId);
	}

	@Transactional
	public OrderInfo createOrder(User user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		long orderId = orderDao.insert(orderInfo);
		RushOrder rushOrder = new RushOrder();
		rushOrder.setGoodsId(goods.getId());
		rushOrder.setOrderId(orderId);
		rushOrder.setUserId(user.getId());
		orderDao.insertRushOrder(rushOrder);
		return orderInfo;
	}
	
}
