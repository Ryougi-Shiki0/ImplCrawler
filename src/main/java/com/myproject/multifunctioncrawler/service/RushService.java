package com.myproject.multifunctioncrawler.service;

import com.myproject.multifunctioncrawler.pojo.OrderInfo;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.vo.GoodsVo;

import java.awt.image.BufferedImage;
import java.util.List;

public interface RushService {
    OrderInfo rush(User user, GoodsVo goods);

    boolean checkPath(User user, long goodsId, String path);

    long getRushResult(Long userId, long goodsId);

    void reset(List<GoodsVo> goodsList);

    boolean checkVerifyCode(User user, long goodsId, int verifyCode);

    String createRushPath(User user, long goodsId);

    BufferedImage createVerifyCode(User user, long goodsId);
}
