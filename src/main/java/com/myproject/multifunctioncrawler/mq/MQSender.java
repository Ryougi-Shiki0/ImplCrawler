package com.myproject.multifunctioncrawler.mq;

import com.myproject.multifunctioncrawler.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


/**
 * @author Arthas
 */
@Slf4j
@Component
public class MQSender {
    @Autowired
    KafkaTemplate kafkaTemplate;

    public void send(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send message:"+msg);
        kafkaTemplate.send("RUSH",msg);
    }

    public void sendTopic(Object message){
        String msg= RedisService.beanToString(message);
        log.info(msg);
    }
}
