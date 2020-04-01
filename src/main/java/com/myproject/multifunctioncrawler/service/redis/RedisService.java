package com.myproject.multifunctioncrawler.service.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {
    @Autowired
    JedisPool jedisPool;

    /**
     * 查询对应键值
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix,String key,Class<T> clazz){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String realkey=prefix.getPrefix()+key;
            String str=jedis.get(realkey);
            T t=stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 插入键值
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix,String key,T value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String str=beanToString(value);
            if (str == null || str.length() < 1) {
                return false;
            }
            String realkey=prefix.getPrefix()+key;
            int seconds=prefix.expireSeconds();
            if(seconds<=0){
                jedis.set(realkey,str);
            }
            else {
                jedis.setex(realkey,seconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            long ret =  jedis.del(realKey);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(KeyPrefix prefix) {
        if(prefix == null) {
            return false;
        }
        List<String> keys = scanKeys(prefix.getPrefix());
        if(keys==null || keys.size() <= 0) {
            return true;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(keys.toArray(new String[0]));
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> scanKeys(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<String>();
            String cursor = "0";
            ScanParams sp = new ScanParams();
            sp.match("*"+key+"*");
            sp.count(100);
            do{
                ScanResult<String> ret = jedis.scan(cursor, sp);
                List<String> result = ret.getResult();
                if(result!=null && result.size() > 0){
                    keys.addAll(result);
                }
                //再处理cursor
                cursor = ret.getStringCursor();
            }while(!"0".equals(cursor));
            return keys;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 判定是否存在
     * @param prefix 标识前缀
     * @param key 键
     * @param <T>
     * @return
     */
    public <T> boolean exists(KeyPrefix prefix,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String realkey=prefix.getPrefix()+key;
            return jedis.exists(realkey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加对应键的值
     * @param prefix 标识前缀
     * @param key 键
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String realkey=prefix.getPrefix()+key;
            return jedis.incr(realkey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少对应键的值
     * @param prefix 标识前缀
     * @param key 键
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String realkey=prefix.getPrefix()+key;
            return jedis.decr(realkey);
        }finally {
            returnToPool(jedis);
        }
    }

    private void returnToPool(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }

    public static <T> T stringToBean(String str,Class<T> clazz){
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    public static <T> String beanToString(T value){
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }
}
