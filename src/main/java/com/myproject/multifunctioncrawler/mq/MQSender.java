package com.myproject.multifunctioncrawler.mq;

import com.myproject.multifunctioncrawler.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MQSender {
    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.RUSH_QUEUE, msg);
    }

    public void sendTopic(Object message){
        String msg= RedisService.beanToString(message);
        log.info(msg);
        amqpTemplate.convertAndSend(MQConfig.RUSH_QUEUE,msg);
    }
}
