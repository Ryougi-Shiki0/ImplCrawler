package com.myproject.multifunctioncrawler.service.Impl;

import com.myproject.multifunctioncrawler.dao.GoodsDao;
import com.myproject.multifunctioncrawler.pojo.Goods;
import com.myproject.multifunctioncrawler.pojo.RushGoods;
import com.myproject.multifunctioncrawler.service.GoodsService;
import com.myproject.multifunctioncrawler.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsDao goodsDao;

    @Override
    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    @Override
    public boolean reduceStock(GoodsVo goods) {
        RushGoods g = new RushGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(g);
        return ret > 0;
    }

    @Override
    public void resetStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList ) {
            RushGoods g = new RushGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDao.resetStock(g);
        }
    }
}
