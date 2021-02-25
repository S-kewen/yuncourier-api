package com.boot.yuncourier.util;

/**
 * @Author: skwen
 * @Description: Rc4Util
 * @Date: 2020-09-20
 */

public interface Rc4Util {
    String decry_RC4(String data, String key);
    String encry_RC4_string(String data, String key);
}
