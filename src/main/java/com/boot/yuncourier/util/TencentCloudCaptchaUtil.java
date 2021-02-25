package com.boot.yuncourier.util;

/**
 * @Author: skwen
 * @Description: TencentCloudApiService-騰訊雲Api接口
 * @Date: 2020-02-01
 */

public interface TencentCloudCaptchaUtil {
    boolean codeResponse(String userIp, String ticket, String randstr);
}
