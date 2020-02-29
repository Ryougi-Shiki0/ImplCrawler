package com.myproject.multifunctioncrawler.service.redis;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
