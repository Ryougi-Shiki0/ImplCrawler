package com.myproject.multifunctioncrawler.mq;

import com.alibaba.fastjson.JSON;
import com.myproject.multifunctioncrawler.pojo.RushMessage;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    RedisService redisService;

    //发送消息方法
    public void sendRushMessage(RushMessage message) {
        String msg = redisService.beanToString(message);
        log.info("send message:"+msg);
        kafkaTemplate.send("RushQueue", msg);
    }


}
