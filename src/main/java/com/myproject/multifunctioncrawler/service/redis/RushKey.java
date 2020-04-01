package com.myproject.multifunctioncrawler.service.redis;

public class RushKey extends BasePrefix{

	private RushKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static RushKey isGoodsOver = new RushKey(0, "go");
	public static RushKey getRushPath = new RushKey(60, "mp");
	public static RushKey getRushVerifyCode = new RushKey(300, "vc");
}
