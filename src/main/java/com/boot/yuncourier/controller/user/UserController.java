package com.boot.yuncourier.controller.user;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.annotation.PassToken;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.LoginRecord;
import com.boot.yuncourier.entity.user.News;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.entity.user.UserIndex;
import com.boot.yuncourier.service.user.LoginRecordService;
import com.boot.yuncourier.service.user.NewsService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: skwen
 * @Description: UserController-用戶相關
 * @Date: 2020-01-31
 */
@RestController
@RequestMapping("/api")
public class UserController {
    @Value("${jwt.config.ttl}")
    private long ttl;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.account}")
    private String account;
    @Value("${mail.password}")
    private String password;
    @Value("${rsa.privateKey}")
    private String privateKey;

    @Autowired
    private UserService userService;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TencentCloudCaptchaUtil tencentcloudapiService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private Util util;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SmtpMailUtil smtpMailUtil;
    @Autowired
    private RSAEncryptUtil rsaEncryptUtil;

    @PassToken
    @RequestMapping("userLogin")
    public Map<String, Object> userLogin(String username, String password, String vall, String randstr) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        if (!tencentcloudapiService.codeResponse(util.getLocalIp(request), vall, randstr)) {
            map.put("status", -1);
            map.put("tip", "驗證碼錯誤");
            return map;
        }
        User user = new User();
        user.setUsername(username);
        user = userService.getByUsername(user);
        if (user == null) {
            map.put("status", -2);
            map.put("tip", "用戶名或密碼錯誤");//用戶不存在
        } else {//用戶存在
            LoginRecord loginRecord = new LoginRecord();
            loginRecord.setUser_id(user.getId());
            loginRecord.setType(1);
            loginRecord.setIp(util.getLocalIp(request));
            loginRecord.setPosition(util.getIpAddressesByIp(util.getLocalIp(request)));
            loginRecord.setSystem(util.getClientSystem(request));
            loginRecord.setBrowser(util.getClientBrowser(request));
            loginRecord.setHeader(request.getHeader("user-agent"));
            user.setPassword(password);
            user = userService.userLogin(user);
            if (user == null) {//密碼錯誤
                loginRecord.setState(2);
                loginRecordService.addLoginRecordByLoginRecord(loginRecord);
                map.put("status", -2);
                map.put("tip", "用戶名或密碼錯誤");
            } else {
                if (user.getState() == 1) {
                    loginRecord.setState(1);
                    loginRecordService.addLoginRecordByLoginRecord(loginRecord);
                    map.put("status", 0);
                    map.put("tip", "登錄成功");
                    Token token = new Token();
                    token.setId(user.getId());
                    token.setIp(util.getMd5(util.getLocalIp(request)));
                    token.setUsername(user.getUsername());
                    token.setPassword(user.getPassword());
                    token.setRole("user");
                    String tokenStr = tokenUtil.createToken(token);
                    map.put("token", tokenStr);
                    map.put("ttl", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new Date().getTime() + ttl)));
                } else if (user.getState() == 2) {
                    loginRecord.setState(3);
                    loginRecordService.addLoginRecordByLoginRecord(loginRecord);
                    map.put("status", -3);
                    map.put("tip", "該賬號被禁止登錄");
                } else if (user.getState() == 3) {
                    loginRecord.setState(4);
                    loginRecordService.addLoginRecordByLoginRecord(loginRecord);
                    map.put("status", -15);
                    map.put("tip", "該賬號已被凍結");
                }
            }
        }
        return map;
    }

    @LoginToken
    @RequestMapping("getIndexInfo")
    public Map<String, Object> getIndexInfo(HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            News news = new News();
            news.setUser_id(token.getId());
            news.setState(1);
            map.put("status", 0);
            map.put("tip", "success");
            map.put("username", token.getUsername());
            map.put("messageState", newsService.getCountByNews(news));
        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @LoginToken
    @RequestMapping("modifyInfo")
    public Map<String, Object> getIndexInfo(String mail, String phone) {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            user.setId(token.getId());
            user.setEmail(mail);
            user.setPhone(phone);
            int count = userService.updateUserInfoByUser(user);
            if (count > 0) {
                map.put("status", 0);
                map.put("tip", "success");
            } else {
                map.put("status", -7);
                map.put("tip", "修改失敗,請稍候再試");
            }

        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @LoginToken
    @RequestMapping("modifyPassword")
    public Map<String, Object> modifyPassword(String oldPassword, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            user.setUsername(token.getUsername());
            user.setPassword(oldPassword);
            user = userService.userLogin(user);
            if (user != null) {
                user.setPassword(newPassword);
                int count = userService.updatePasswordByUser(user);
                if (count > 0) {
                    map.put("status", 0);
                    map.put("tip", "success");
                } else {
                    map.put("status", -7);
                    map.put("tip", "修改失敗,請稍候再試");
                }
            } else {
                map.put("status", -8);
                map.put("tip", "原密碼錯誤,請檢查後再試");
            }
        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @LoginToken
    @RequestMapping("getModifyInfo")
    public Map<String, Object> getModifyInfo() {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            user.setId(token.getId());
            user = userService.getById(user);
            if (user != null) {
                map.put("status", 0);
                map.put("tip", "success");
                map.put("mail", user.getEmail());
                map.put("phone", user.getPhone());
            } else {
                map.put("status", -7);
                map.put("tip", "查詢用戶數據失敗,請稍候再試");
            }

        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @LoginToken
    @RequestMapping("getUserIndexInfo")
    public Map<String, Object> getUserIndexInfo(HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            UserIndex userIndex = new UserIndex();
            user.setId(token.getId());
            userIndex = userService.getUserIndexByUser(user);
            map.put("status", 0);
            map.put("tip", "success");
            map.put("todayMailNum", userIndex.getTodayMailNum());
            map.put("allMailNum", userIndex.getAllMailNum());
            map.put("todaySmsNum", userIndex.getTodaySmsNum());
            map.put("allSmsNum", userIndex.getAllSmsNum());
            map.put("todayLinkNum", userIndex.getTodayLinkNum());
            map.put("allLinkNum", userIndex.getAllLinkNum());
            map.put("allFailNum", userIndex.getMailFailNum() + userIndex.getSmsFailNum() + userIndex.getLinkFailNum());
            map.put("todayPay", userIndex.getTodayPay());
            map.put("allPay", userIndex.getAllPay());
            map.put("username", userIndex.getUsername());
            map.put("balance", userIndex.getBalance());
            map.put("email", userIndex.getEmail());
            map.put("ip", util.getLocalIp(request));
            map.put("phone", userIndex.getPhone());
            map.put("state", userIndex.getState());
        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @PassToken
    @RequestMapping("checkToken")
    public Map<String, Object> checkToken() {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", new Date().getTime());
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            if (token.getExpire_time().after(new Date())) {
                User user = new User();
                user.setId(token.getId());
                user = userService.getById(user);
                if (user != null) {
                    if (user.getState() == 1) {
                        if (util.getMd5(user.getPassword()).equals(token.getPassword())) {
                            map.put("status", 0);
                            map.put("tip", "success");
                            map.put("username", token.getUsername());
                            map.put("ttl", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(token.getExpire_time()));
                            if ("1".equals(redisUtil.get("checkTokenByIp"))) {
                                if (!util.getMd5(util.getLocalIp(request)).equals(token.getIp())) {
                                    map.remove("username");
                                    map.remove("ttl");
                                    map.put("status", -1);
                                    map.put("tip", "用戶IP異常，請重新登錄");
                                }
                            }
                        } else {
                            map.put("status", -1);
                            map.put("tip", "用戶密碼錯誤");
                        }
                    } else {
                        map.put("status", -2);
                        map.put("tip", "用戶狀態異常");
                    }
                } else {
                    map.put("status", -3);
                    map.put("tip", "用戶不存在");
                }
            } else {
                map.put("status", -5);
                map.put("tip", "登錄已到期");
            }
        } else {
            map.put("status", -4);
            map.put("tip", "登錄已失效");
        }
        return map;
    }

    @PassToken
    @RequestMapping("resetPassword")
    public Map<String, Object> resetPassword(String username) {
        Map<String, Object> map = new HashMap<>();
        if (username != null && username != "") {
            User user = new User();
            user.setUsername(username);
            user = userService.getByUsername(user);
            if (user != null) {
                if (user.getState() == 3) {
                    String newPassword = util.getRandomStr(2, 16, "");
                    Map<String, String> result = smtpMailUtil.sendEasyMail(host, account, password, user.getEmail(), "yunCourier-重置密碼", "密碼已重置,請妥善保管新密碼,新密碼:[" + newPassword + "]");
                    if ("true".equals(result.get("status"))) {
                        user.setPassword(util.getMd5(newPassword));
                        userService.resetPasswordByUser(user);
                        map.put("status", 0);
                        map.put("tip", "success");
                    } else {
                        map.put("status", -14);
                        map.put("tip", "重置失敗,請稍候再試");
                    }
                } else {
                    map.put("status", -1);
                    map.put("tip", "該用戶不允許重置密碼");
                }
            } else {
                map.put("status", -1);
                map.put("tip", "該用戶不允許重置密碼");
            }
        } else {
            map.put("status", -1);
            map.put("tip", "用戶名不能為空");
        }
        return map;
    }

    @PassToken
    @RequestMapping("loginByRsa")
    public Map<String, Object> loginByRsa(String username, String password) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (username != null && password != "") {
            User user = new User();
            user.setUsername(username);
            user.setPassword(rsaEncryptUtil.decrypt(password, privateKey));
            user = userService.userLogin(user);
            if (user != null) {
                if (user.getState() == 1) {
                    map.put("status", 0);
                    map.put("tip", "success");
                } else {
                    map.put("status", -1);
                    map.put("tip", "該用戶已被禁止登錄");
                }
            } else {
                map.put("status", -1);
                map.put("tip", "用戶名或密碼錯誤");
            }
        } else {
            map.put("status", -1);
            map.put("tip", "用戶名密碼不能為空");
        }
        return map;
    }
}
