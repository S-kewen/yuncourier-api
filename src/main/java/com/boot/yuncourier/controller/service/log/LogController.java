package com.boot.yuncourier.controller.service.log;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.annotation.PassToken;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.service.log.Log;
import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.service.log.LogService;
import com.boot.yuncourier.service.service.var.VarService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: skwen
 * @ClassName: LogController
 * @Description: Conteoller
 * @Date: 2020-04-29
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class LogController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private LogService logService;
    @Autowired
    private HttpServletRequest request;

    @LoginToken
    @RequestMapping("listLog")
    public Object listLog(int pageNumber, int pageSize, String content, String startTime, String endTime) throws JSONException, ParseException {
        JSONObject json = new JSONObject();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            user.setId(token.getId());
            user.setContent(content);
            if (startTime != null && startTime != "") {
                user.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if (endTime != null && endTime != "") {
                user.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }
            PageHelper.startPage(pageNumber, pageSize);
            List<Log> select = logService.getList(user);
            PageInfo<Log> pageInfo = new PageInfo<>(select);
            return pageInfo;
        } else {
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }

    @LoginToken
    @RequestMapping("deleteLog")
    public Map<String, Object> deleteLog(int id) {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            Log log = new Log();
            log.setUser_id(token.getId());
            log.setId(id);
            int count = logService.deleteOne(log);
            if (count > 0) {
                map.put("status", 0);
                map.put("tip", "success");
            } else {
                map.put("status", -2);
                map.put("tip", "刪除失敗,請稍候再試");
            }
        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
}
