package com.myproject.multifunctioncrawler.controller;

import com.myproject.multifunctioncrawler.access.AccessLimit;
import com.myproject.multifunctioncrawler.mq.MQSender;
import com.myproject.multifunctioncrawler.mq.RushMessage;
import com.myproject.multifunctioncrawler.pojo.RushOrder;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.result.CodeMsg;
import com.myproject.multifunctioncrawler.result.Result;
import com.myproject.multifunctioncrawler.service.GoodsService;
import com.myproject.multifunctioncrawler.service.OrderService;
import com.myproject.multifunctioncrawler.service.RushService;
import com.myproject.multifunctioncrawler.service.UserService;
import com.myproject.multifunctioncrawler.service.redis.GoodsKey;
import com.myproject.multifunctioncrawler.service.redis.OrderKey;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import com.myproject.multifunctioncrawler.service.redis.RushKey;
import com.myproject.multifunctioncrawler.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping("/rush")
public class RushController implements InitializingBean {
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

    @Autowired
    MQSender sender;

    private HashMap<Long, Boolean> localOverMap =  new HashMap<>();

    @RequestMapping(value="/{path}/do_rush", method=RequestMethod.POST)
    @ResponseBody
    public Result<Integer> rush(Model model,User user,
                                   @RequestParam("goodsId")long goodsId,
                                   @PathVariable("path") String path) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);
        //验证path
        boolean check = rushService.checkPath(user, goodsId, path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over) {
            return Result.error(CodeMsg.RUSH_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, ""+goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.RUSH_OVER);
        }
        //判断是否已经秒杀到了
        RushOrder order = orderService.getRushOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_RUSH);
        }
        //入队
        RushMessage rm = new RushMessage();
        rm.setUser(user);
        rm.setGoodsId(goodsId);
        sender.send(rm);
        //排队中
        return Result.success(0);
    }

    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> rushResult(Model model,User user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result  =rushService.getRushResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null) {
            return;
        }
        for(GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    @RequestMapping(value="/reset", method= RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for(GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getRushOrderByUidGid);
        redisService.delete(RushKey.isGoodsOver);
        rushService.reset(goodsList);
        return Result.success(true);
    }

    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @RequestMapping(value="/path", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getRushPath(HttpServletRequest request,User user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value="verifyCode", defaultValue="0")int verifyCode
    ) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = rushService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path  =rushService.createRushPath(user, goodsId);
        return Result.success(path);
    }

    @RequestMapping(value="/verifyCode", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getRushVerifyCode(HttpServletResponse response, User user,
                                               @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image  = rushService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.RUSH_FAIL);
        }
    }
}
