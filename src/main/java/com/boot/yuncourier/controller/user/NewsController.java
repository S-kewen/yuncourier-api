package com.boot.yuncourier.controller.user;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.user.News;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.user.LoginRecordService;
import com.boot.yuncourier.service.user.NewsService;
import com.boot.yuncourier.service.user.TransactionService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.TokenUtil;
import com.boot.yuncourier.util.Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: skwen
 * @Description: NewsController-系統消息
 * @Date: 2020-01-31
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class NewsController {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private Util util;
    @Autowired
    private HttpServletRequest request;

    @LoginToken
    @RequestMapping("listMessage")
    public Object listMessage(int pageNumber,int pageSize,String content,int state,String startTime,String endTime) throws JSONException, ParseException {
        JSONObject json=new JSONObject();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            User user = new User();
            user.setId(token.getId());
            user.setContent(content);
            user.setState(state);
            if(startTime!=null && startTime!=""){
                user.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if(endTime!=null && endTime!=""){
                user.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }
            PageHelper.startPage(pageNumber,pageSize);
            List<News> select = newsService.getNewsListByNews(user);
            PageInfo<News> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteMessage")
    public Map<String, Object> deleteMessage(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            News news = new News();
            news.setUser_id(token.getId());
            news.setId(id);
            int count=newsService.deleteNewsByNews(news);
            if (count>0){
                map.put("status",0);
                map.put("tip","success");
            }else{
                map.put("status",-7);
                map.put("tip","刪除失敗,請稍候再試");
            }

        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
    @LoginToken
    @RequestMapping("readMessage")
    public Map<String, Object> readMessage(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            News news = new News();
            news.setUser_id(token.getId());
            news.setId(id);
            news.setState(2);
            int count=newsService.updateNewsByNews(news);
            if (count>0){
                map.put("status",0);
                map.put("tip","success");
            }else{
                map.put("status",-7);
                map.put("tip","設置失敗,請稍候再試");
            }

        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
    @LoginToken
    @RequestMapping("allReadMessage")
    public Map<String, Object> allReadMessage() {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            User user = new User();
            user.setId(token.getId());
            int count=newsService.allReadByNews(user);
            if (count>0){
                map.put("status",0);
                map.put("tip","success");
                map.put("count",count);
            }else{
                map.put("status",-7);
                map.put("tip","設置失敗,請稍候再試");
            }

        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
    @LoginToken
    @RequestMapping("getUnreadMessageCount")
    public Map<String, Object> getUnreadMessageCount() {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            News news = new News();
            news.setUser_id(token.getId());
            news.setState(1);
            int count=newsService.getCountByNews(news);
            map.put("status",0);
            map.put("tip","success");
            map.put("unreadMessageCount",count);
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
    @LoginToken
    @RequestMapping("listUserIndexMessage")
    public Object listUserIndexMessage() throws JSONException, ParseException {
        JSONObject json=new JSONObject();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            User user = new User();
            user.setId(token.getId());
            PageHelper.startPage(1,50);
            List<News> select = newsService.getUserIndexNewsListByUser(user);
            PageInfo<News> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }

}
