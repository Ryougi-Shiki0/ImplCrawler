package com.myproject.multifunctioncrawler.service.Impl;

import com.myproject.multifunctioncrawler.pojo.OrderInfo;
import com.myproject.multifunctioncrawler.pojo.RushOrder;
import com.myproject.multifunctioncrawler.pojo.User;
import com.myproject.multifunctioncrawler.service.GoodsService;
import com.myproject.multifunctioncrawler.service.OrderService;
import com.myproject.multifunctioncrawler.service.RushService;
import com.myproject.multifunctioncrawler.service.redis.RedisService;
import com.myproject.multifunctioncrawler.service.redis.RushKey;
import com.myproject.multifunctioncrawler.utils.MD5Util;
import com.myproject.multifunctioncrawler.utils.UUIDUtil;
import com.myproject.multifunctioncrawler.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

@Service
public class RushServiceImpl implements RushService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;

	@Autowired
	private RedisService redisService;

	@Override
	@Transactional
	public OrderInfo rush(User user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		goodsService.reduceStock(goods);
		//order_info maiosha_order
		return orderService.createOrder(user, goods);
	}

	@Override
	public void reset(List<GoodsVo> goodsList) {
		goodsService.resetStock(goodsList);
		orderService.deleteOrders();
	}

	@Override
	public boolean checkPath(User user, long goodsId, String path) {
		if(user == null || path == null) {
			return false;
		}
		String pathOld = redisService.get(RushKey.getRushPath, ""+user.getId() + "_"+ goodsId, String.class);
		return path.equals(pathOld);
	}

	@Override
	public long getRushResult(Long userId, long goodsId) {
		RushOrder order = orderService.getRushOrderByUserIdGoodsId(userId, goodsId);
		//秒杀成功
		if(order != null) {
			return order.getOrderId();
		}else {
			boolean isOver = getGoodsOver(goodsId);
			if(isOver) {
				return -1;
			}else {
				return 0;
			}
		}
	}

	private void setGoodsOver(Long goodsId) {
		redisService.set(RushKey.isGoodsOver, ""+goodsId, true);
	}

	private boolean getGoodsOver(long goodsId) {
		return redisService.exists(RushKey.isGoodsOver, ""+goodsId);
	}


	@Override
	public String createRushPath(User user, long goodsId) {
		if(user == null || goodsId <=0) {
			return null;
		}
		String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
		redisService.set(RushKey.getRushPath, ""+user.getId() + "_"+ goodsId, str);
		return str;
	}

	@Override
	public BufferedImage createVerifyCode(User user, long goodsId) {
		if(user == null || goodsId <=0) {
			return null;
		}
		int width = 80;
		int height = 32;
		//create the image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		// set the background color
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// draw the border
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		// create a random instance to generate the codes
		Random rdm = new Random();
		// make some confusion
		for (int i = 0; i < 50; i++) {
			int x = rdm.nextInt(width);
			int y = rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		// generate a random code
		String verifyCode = generateVerifyCode(rdm);
		g.setColor(new Color(0, 100, 0));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();
		//把验证码存到redis中
		int rnd = calc(verifyCode);
		redisService.set(RushKey.getRushVerifyCode, user.getId()+","+goodsId, rnd);
		//输出图片
		return image;
	}


	@Override
	public boolean checkVerifyCode(User user, long goodsId, int verifyCode) {
		if(user == null || goodsId <=0) {
			return false;
		}
		Integer codeOld = redisService.get(RushKey.getRushVerifyCode, user.getId()+","+goodsId, Integer.class);
		if(codeOld == null || codeOld - verifyCode != 0 ) {
			return false;
		}
		redisService.delete(RushKey.getRushVerifyCode, user.getId()+","+goodsId);
		return true;
	}

	private static int calc(String exp) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			return (Integer)engine.eval(exp);
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private static char[] ops = new char[] {'+', '-', '*'};
	/**
	 * + - *
	 * */
	private String generateVerifyCode(Random rdm) {
		int num1 = rdm.nextInt(10);
		int num2 = rdm.nextInt(10);
		int num3 = rdm.nextInt(10);
		char op1 = ops[rdm.nextInt(3)];
		char op2 = ops[rdm.nextInt(3)];
		String exp = ""+ num1 + op1 + num2 + op2 + num3;
		return exp;
	}
	
}
