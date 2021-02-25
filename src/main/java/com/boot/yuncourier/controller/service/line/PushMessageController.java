package com.boot.yuncourier.controller.service.line;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.service.line.PushMessage;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.service.line.LineConfigService;
import com.boot.yuncourier.service.service.line.PushMessageService;
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
 * @ClassName: PushMessageController
 * @Description: Conteoller
 * @Date: 2020-03-21
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class PushMessageController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private Util util;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private PushMessageService pushMessageService;
    @Autowired
    private HttpServletRequest request;
    @LoginToken
    @RequestMapping("listPushMessage")
    public Object listPushMessage(int pageNumber,int pageSize,String content,int state,String startTime,String endTime) throws JSONException, ParseException {
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
            List<PushMessage> select = pushMessageService.getList(user);
            PageInfo<PushMessage> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deletePushMessage")
    public Map<String, Object> deletePushMessage(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            PushMessage pushMessage= new PushMessage();
            pushMessage.setUser_id(token.getId());
            pushMessage.setId(id);
            int count=pushMessageService.deleteOne(pushMessage);
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
    @RequestMapping("getPushMessageInfo")
    public Map<String, Object> getPushMessageInfo(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            PushMessage pushMessage= new PushMessage();
            pushMessage.setUser_id(token.getId());
            pushMessage.setId(id);
            pushMessage=pushMessageService.getInfo(pushMessage);
            if (pushMessage!=null){
                map.put("status",0);
                map.put("tip","success");
                map.put("lineConfigId",pushMessage.getLine_config_id());
                map.put("lineConfigName",pushMessage.getLine_config_name());
                map.put("userId",pushMessage.getTo());
                map.put("type",pushMessage.getType());
                map.put("state",pushMessage.getState());
                map.put("text",pushMessage.getText());
                map.put("ip",pushMessage.getIp());
                map.put("remark",pushMessage.getRemark());
                map.put("addTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pushMessage.getAdd_time()));
            }else{
                map.put("status",-7);
                map.put("tip","該記錄不存在");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
}
