package com.boot.yuncourier.controller.user;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.Transaction;
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
 * @Description: TransactionController-交易記錄
 * @Date: 2020-01-31
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class TransactionController {
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
    @RequestMapping("listTransaction")
    public Object listTransaction(int pageNumber,int pageSize,String title,int state,String startTime,String endTime) throws JSONException, ParseException {
        JSONObject json=new JSONObject();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            User user = new User();
            user.setId(token.getId());
            user.setContent(title);
            user.setState(state);
            if(startTime!=null && startTime!=""){
                user.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if(endTime!=null && endTime!=""){
                user.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }
            PageHelper.startPage(pageNumber,pageSize);
            List<Transaction> select = transactionService.getTransactionListByUser(user);
            PageInfo<Transaction> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteTransaction")
    public Map<String, Object> deleteTransaction(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Transaction transaction = new Transaction();
            transaction.setUser_id(token.getId());
            transaction.setId(id);
            int count=transactionService.deleteTransactionByTransaction(transaction);
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
}
