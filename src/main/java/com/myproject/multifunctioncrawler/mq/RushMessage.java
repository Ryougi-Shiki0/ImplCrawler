package com.myproject.multifunctioncrawler.mq;

import com.myproject.multifunctioncrawler.pojo.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RushMessage {
    private User user;
    private long goodsId;
}
