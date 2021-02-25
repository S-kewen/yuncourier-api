package com.boot.yuncourier.util.impl;

import com.boot.yuncourier.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: skwen
 * @Description: RedisServiceImpl-redis數據庫操作service
 * @Date: 2020-02-01
 */

@Component
public class RedisUtilmpl implements RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void set(String key, Object value,int timeout) {
        if (timeout==0){
            redisTemplate.boundValueOps(key).set(value);
        }else{
            redisTemplate.boundValueOps(key).set(value,timeout, TimeUnit.SECONDS);
        }

    }
    @Override
    public Object get(String key) {
        return redisTemplate.boundValueOps(key).get();
    }
}
