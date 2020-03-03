package com.myproject.multifunctioncrawler.controller;

import com.myproject.multifunctioncrawler.pojo.OrderInfo;
import com.myproject.multifunctioncrawler.pojo.RushOrder;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.result.CodeMsg;
import com.myproject.multifunctioncrawler.service.GoodsService;
import com.myproject.multifunctioncrawler.service.OrderService;
import com.myproject.multifunctioncrawler.service.RushService;
import com.myproject.multifunctioncrawler.service.UserService;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import com.myproject.multifunctioncrawler.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/rush")
public class RushController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RushService rushService;

    @RequestMapping("/do_rush")
    public String list(HttpServletRequest request,Model model, User user,
                       @RequestParam("goodsId")long goodsId) {
        if(request.getHeader("Referer").equals("no-referrer-when-downgrade") || user == null){
            return "login";
        }
        model.addAttribute("user", user);
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.RUSH_OVER.getMsg());
            return "rush_fail";
        }
        //判断是否已经秒杀到了
        RushOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_RUSH.getMsg());
            return "rush_fail";
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = rushService.rush(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}
