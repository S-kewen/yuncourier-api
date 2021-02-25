package com.boot.yuncourier.util;

/**
 * @Author: skwen
 * @Description: RedisService-redis數據庫操作接口
 * @Date: 2020-02-01
 */

public interface RedisUtil {
    void set(String key, Object value,int timeout);
    Object get(String key);
}
