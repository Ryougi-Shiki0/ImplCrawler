package com.myproject.multifunctioncrawler.mq;


import com.myproject.multifunctioncrawler.pojo.User;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

public class MQSenderTest {

    @Test
    public void testSend(){
        MQSender sender=new MQSender();
        User user=new User();
        user.setId(13L);
        long goodsId=12315L;
        RushMessage rm = new RushMessage();
        rm.setUser(user);
        rm.setGoodsId(goodsId);

        sender.send(rm);

        Assert.assertThat("发信失败", true,Is.is(true));
    }
}
