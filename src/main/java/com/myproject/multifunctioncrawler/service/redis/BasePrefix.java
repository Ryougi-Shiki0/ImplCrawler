package com.myproject.multifunctioncrawler.service.redis;

public abstract class BasePrefix implements KeyPrefix{
    private int expireSecond;
    private String prefix;

    public BasePrefix(String prefix){
        this(0,prefix);
    }

    public BasePrefix(int expireSecond,String prefix){
        this.expireSecond=expireSecond;
        this.prefix=prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSecond;
    }

    @Override
    public String getPrefix() {
        String className=getClass().getSimpleName();
        return className+":"+prefix;
    }
}
