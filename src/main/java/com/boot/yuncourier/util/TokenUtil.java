package com.boot.yuncourier.util;

import com.boot.yuncourier.entity.system.Token;

/**
 * @Author: skwen
 * @Description: TokenService-token接口
 * @Date: 2020-02-01
 */

public interface TokenUtil {
//    String getToken(Token token);
    String createToken(Token token);
    Token parseToken(String tokenStr);
}
