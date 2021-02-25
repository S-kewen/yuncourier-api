package com.boot.yuncourier.controller.home;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.home.Console;
import com.boot.yuncourier.entity.system.Performance;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.Transaction;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.console.ConsoleService;
import com.boot.yuncourier.service.user.TransactionService;
import com.boot.yuncourier.util.TokenUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: skwen
 * @Description: ConsoleController-控制台
 * @Date: 2020-01-30
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class ConsoleController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ConsoleService consoleService;
    @Autowired
    private com.boot.yuncourier.service.system.PerformanceService PerformanceService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private TransactionService transactionService;

    @LoginToken
    @RequestMapping("getConsoleInfo")
    public Map<String, Object> getConsoleInfo() {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            Console console = new Console();
            user.setId(token.getId());
            console = consoleService.getConsoleInfoByUser(user);
            map.put("status", 0);
            map.put("tip", "success");
            map.put("todayRunNum", console.getTodayMailNum() + console.getTodaySmsNum() + console.getTodayLinkNum() + console.getTodayLineNum());
            map.put("allRunNum", console.getAllMailNum() + console.getAllSmsNum() + console.getAllLinkNum());
            map.put("todayPay", console.getTodayPay());
            map.put("allPay", console.getAllPay());
            map.put("mailRunNum", console.getAllMailNum());
            map.put("smsRunNum", console.getAllSmsNum());
            map.put("linkRunNum", console.getAllLinkNum());
            map.put("lineRunNum", console.getAllLineNum());
            map.put("interceptNum", console.getAllInterceptNum());
            map.put("runFailNum", console.getMailFailNum() + console.getSmsFailNum() + console.getLinkFailNum() + console.getLineFailNum());
            map.put("cpu", console.getCpu());
            map.put("ram", console.getRam());
        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }

    @LoginToken
    @RequestMapping("getEchatsInfoByPerformance")
    public Object getEchatsInfoByPerformance(String nowDate) throws JSONException, ParseException {
        JSONObject json = new JSONObject();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            List<Performance> performanceList;
            user.setId(token.getId());
            Performance performance = new Performance();
            Date now = new SimpleDateFormat("yyyy-MM-dd").parse(nowDate);
            performance.setAdd_time(now);
            performanceList = PerformanceService.getPerformanceAvgListByPerformance(performance);
            return performanceList;
        } else {
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json.toString();
    }

    @LoginToken
    @RequestMapping("getEchatsInfoByApiType")
    public Map<String, Object> getEchatsInfoByApiType() {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            Console console = new Console();
            user.setId(token.getId());
            console = consoleService.getApiCountByUser(user);
            map.put("status", 0);
            map.put("tip", "success");
            map.put("api_mail_num", console.getAllMailNum());
            map.put("api_sms_num", console.getAllSmsNum());
            map.put("api_link_num", console.getAllLinkNum());
            map.put("api_line_num", console.getAllLineNum());
        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }

    @LoginToken
    @RequestMapping("getEchatsInfoByPay")
    public Object getEchatsInfoByPay(String nowDate) throws JSONException, ParseException {
        JSONObject json = new JSONObject();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            List<Transaction> transactionList;
            user.setId(token.getId());
            Date now = new SimpleDateFormat("yyyy-MM-dd").parse(nowDate);
            user.setStartTime(now);
            transactionList = transactionService.geyPayListByUser(user);
            return transactionList;
        } else {
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json.toString();
    }
}
