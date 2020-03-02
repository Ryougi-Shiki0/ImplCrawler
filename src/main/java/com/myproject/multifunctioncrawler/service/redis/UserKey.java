package com.myproject.multifunctioncrawler.service.redis;

public class UserKey extends BasePrefix {
    private static final int TOKEN_EXPIRESECONDS=3600;

    private UserKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    public static UserKey token=new UserKey(TOKEN_EXPIRESECONDS,"tk");
}
