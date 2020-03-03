package com.myproject.multifunctioncrawler.controller;

import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.service.GoodsService;
import com.myproject.multifunctioncrawler.service.UserService;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import com.myproject.multifunctioncrawler.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String list(HttpServletRequest request,Model model, User user) {
        if(user.getId() == null || request.getHeader("Referer")==null){
            return "login";
        }
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(HttpServletRequest request,Model model,User user,
                         @PathVariable("goodsId")long goodsId) {
        if(request.getHeader("Referer").equals("no-referrer-when-downgrade") || user == null){
            return "login";
        }
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int rushStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            rushStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            rushStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            rushStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("rushStatus", rushStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}