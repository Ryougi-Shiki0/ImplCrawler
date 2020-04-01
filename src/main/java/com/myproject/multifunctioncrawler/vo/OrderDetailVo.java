package com.myproject.multifunctioncrawler.vo;


import com.myproject.multifunctioncrawler.pojo.OrderInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailVo {
	private GoodsVo goods;
	private OrderInfo order;
}
