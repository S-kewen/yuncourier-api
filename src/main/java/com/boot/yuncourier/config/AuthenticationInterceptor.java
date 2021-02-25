package com.boot.yuncourier.config;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.annotation.PassToken;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.util.Util;
import com.boot.yuncourier.util.RedisUtil;
import com.boot.yuncourier.util.TokenUtil;
import com.boot.yuncourier.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author: skwen
 * @Description: token校驗過濾器
 * @Date: 2020-02-05
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    Util util;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private HttpServletRequest request;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
//        httpServletRequest.getHeader("token")
        String tokenStr =httpServletRequest.getHeader("Authorization");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
               // System.out.println("passToken");
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken userLoginToken = method.getAnnotation(LoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (tokenStr == null || tokenStr=="") {
                    //System.out.println("無token,請重新登錄");
                    throw new RuntimeException("無token,請重新登錄");
                }
                // 获取 token 中的 username
                Token token = new Token();
                try {
                    token = tokenUtil.parseToken(tokenStr);
                } catch (JWTDecodeException j) {
                    //System.out.println("401解析错误");
                    throw new RuntimeException("401,請重新登錄");
            }
                if (new Date().after(token.getExpire_time())){
                    //System.out.println("token已过期");
                    throw new RuntimeException("token已過期,請重新登錄");
                }
                User user = new User();
                user.setUsername(token.getUsername());
                user=userService.getByUsername(user);
                if (user == null) {
                   // System.out.println("token已失效");
                    throw new RuntimeException("token已失效,請重新登錄");//用户不存在，请重新登录
                }else{
                    if(user.getState()!=1){
                        throw new RuntimeException("用戶狀態異常,請重新登錄");//用户狀態異常
                    }
                    if(!token.getPassword().equals(util.getMd5(user.getPassword()))){
                        throw new RuntimeException("用戶密碼錯誤,請重新登錄");//用户狀態異常
                    }
                    if("1".equals(redisUtil.get("checkTokenByIp"))){
                     if(!token.getIp().equals(util.getMd5(util.getLocalIp(request)))){
                        throw new RuntimeException("用戶IP異常,請重新登錄");//用户狀態異常
                    }
                    }
                }
                // 验证 token
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}