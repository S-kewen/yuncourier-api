package com.boot.yuncourier.util;

/**
 * @Author: skwen
 * @Description: Every8dSmsService-every8d短信接口
 * @Date: 2020-02-01
 */

public interface Every8dSmsUtil {
    String sendSms(String subject,String msg,String phone,String sendTime,int retryTime);
}
