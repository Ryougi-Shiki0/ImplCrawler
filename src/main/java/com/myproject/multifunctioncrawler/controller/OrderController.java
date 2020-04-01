package com.myproject.multifunctioncrawler.controller;

import com.myproject.multifunctioncrawler.pojo.OrderInfo;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.result.CodeMsg;
import com.myproject.multifunctioncrawler.result.Result;
import com.myproject.multifunctioncrawler.service.GoodsService;
import com.myproject.multifunctioncrawler.service.OrderService;
import com.myproject.multifunctioncrawler.service.UserService;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import com.myproject.multifunctioncrawler.vo.GoodsVo;
import com.myproject.multifunctioncrawler.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;

	@RequestMapping("/detail")
	@ResponseBody
	public Result<OrderDetailVo> info(Model model,User user,
									  @RequestParam("orderId") long orderId) {
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		OrderInfo order = orderService.getOrderById(orderId);
		if(order == null) {
			return Result.error(CodeMsg.ORDER_NOT_EXIST);
		}
		long goodsId = order.getGoodsId();
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		OrderDetailVo vo = new OrderDetailVo();
		vo.setOrder(order);
		vo.setGoods(goods);
		return Result.success(vo);
	}
    
}
