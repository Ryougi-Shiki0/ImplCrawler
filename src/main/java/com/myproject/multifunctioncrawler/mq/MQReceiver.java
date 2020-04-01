package com.myproject.multifunctioncrawler.mq;

import com.myproject.multifunctioncrawler.pojo.RushOrder;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.service.GoodsService;
import com.myproject.multifunctioncrawler.service.OrderService;
import com.myproject.multifunctioncrawler.service.RushService;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import com.myproject.multifunctioncrawler.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RushService rushService;

    @RabbitListener(queues = MQConfig.RUSH_QUEUE)
    public void receive(String message){
        log.info("receive message:"+message);
        RushMessage mm  = RedisService.stringToBean(message, RushMessage.class);
        User user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        RushOrder order = orderService.getRushOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        rushService.rush(user, goods);
    }
}
