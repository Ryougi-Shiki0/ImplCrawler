package com.myproject.multifunctioncrawler.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {

    public static final String RUSH_QUEUE="rush.queue";

    @Bean
    public Queue queue(){
        return new Queue(RUSH_QUEUE,true);
    }
}
