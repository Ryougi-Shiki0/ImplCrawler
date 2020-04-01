package com.myproject.multifunctioncrawler.service.redis;

public class OrderKey extends BasePrefix {

	public OrderKey(String prefix) {
		super(prefix);
	}
	public static OrderKey getRushOrderByUidGid = new OrderKey("moug");
}
