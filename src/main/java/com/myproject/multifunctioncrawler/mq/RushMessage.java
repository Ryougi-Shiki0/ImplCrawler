package com.myproject.multifunctioncrawler.mq;

import com.myproject.multifunctioncrawler.pojo.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RushMessage {
    private User user;
    private long goodsId;
}
