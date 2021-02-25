package com.boot.yuncourier.util.impl;

import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.util.Util;
import com.boot.yuncourier.util.TokenUtil;
import com.boot.yuncourier.service.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: TokenServiceImpl-tokenservice
 * @Date: 2020-02-01
 */

@Component
public class TokenUtilImpl implements TokenUtil {
    @Value("${jwt.config.salt}")
    private String salt;
    @Value("${jwt.config.ttl}")
    private long ttl;
    @Autowired
    private UserService userService;
    @Autowired
    private Util util;
    @Override
    public String createToken(Token token) {
        JwtBuilder JwtBuilder = Jwts.builder()
                .setId(String.valueOf(token.getId()))
                .setSubject(token.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+ttl))
                .claim("role",token.getRole())
                .claim("ip",token.getIp())
                .claim("hex", util.getMd5(token.getPassword()))
                .signWith(SignatureAlgorithm.HS256,salt);
        return JwtBuilder.compact();
    }
    @Override
    public Token parseToken(String tokenStr) {
        if(tokenStr==null || tokenStr==""){
            return null;
        }
        Claims claims = Jwts.parser()
                .setSigningKey(salt)
                .parseClaimsJws(tokenStr)
                .getBody();
        User user = new User();
        user.setId(Integer.parseInt(claims.getId()));
        user=userService.getById(user);
        if(user!=null){
            if(user.getState()!=1){
                return null;
            }else{
                Token token = new Token();
                token.setId(Integer.parseInt(claims.getId()));
                token.setUsername(claims.getSubject());
                token.setPassword((String) claims.get("hex"));
                token.setAdd_time(claims.getIssuedAt());
                token.setExpire_time(claims.getExpiration());
                token.setRole((String) claims.get("role"));
                token.setIp((String) claims.get("ip"));
                return token;
            }
        }else{
            return null;
        }
    }
}
