package com.boot.yuncourier.controller.service.sms;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.service.sms.Sms;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.mail.MailService;
import com.boot.yuncourier.service.service.sms.SmsService;
import com.boot.yuncourier.service.user.LoginRecordService;
import com.boot.yuncourier.service.user.NewsService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.*;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: skwen
 * @Description: SmsController-我的短信
 * @Date: 2020-01-31
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class SmsController {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private TencentCloudCaptchaUtil tencentcloudapiService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private Util util;
    @Autowired
    private MailService mailService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SmtpMailUtil smtpMailUtil;
    @Autowired
    private Every8dSmsUtil every8DSmsUtil;
    @LoginToken
    @RequestMapping("listMySms")
    public Object listMySms(int pageNumber,int pageSize,String content,int state,int type,String startTime,String endTime) throws JSONException, ParseException {
        JSONObject json=new JSONObject();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            User user = new User();
            user.setId(token.getId());
            user.setContent(content);
            user.setState(state);
            user.setType(type);
            if(startTime!=null && startTime!=""){
                user.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if(endTime!=null && endTime!=""){
                user.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }
            PageHelper.startPage(pageNumber,pageSize);
            List<Sms> select = smsService.getSmsListByUser(user);
            PageInfo<Sms> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteSms")
    public Map<String, Object> deleteSms(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Sms sms = new Sms();
            sms.setUser_id(token.getId());
            sms.setId(id);
            int count=smsService.deleteSmsBySms(sms);
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
    @RequestMapping("getSmsInfo")
    public Map<String, Object> getSmsInfo(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Sms sms = new Sms();
            sms.setUser_id(token.getId());
            sms.setId(id);
            sms=smsService.getSmsInfoBySms(sms);
            if (sms!=null){
                map.put("status",0);
                map.put("tip","success");
                map.put("softwareName",sms.getSoftware_name());
                map.put("softwareId",sms.getSoftware_id());
                map.put("token",sms.getToken());
                map.put("type",sms.getType());
                map.put("state",sms.getState());
                map.put("receive_phone",sms.getReceive_phone());
                map.put("subject",sms.getSubject());
                map.put("msg",sms.getMsg());
                map.put("ip",sms.getIp());
                map.put("addTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sms.getAdd_time()));
                map.put("remark",sms.getRemark());
            }else{
                map.put("status",-11);
                map.put("tip","該短信不存在,請稍候再試");
            }

        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
    @LoginToken
    @RequestMapping("retrySms")
    public Map<String, Object> retrySms(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            String result = null;
            Sms sms = new Sms();
            sms.setUser_id(token.getId());
            sms.setId(id);
            sms=smsService.getSmsInfoBySms(sms);
            if (sms!=null && sms.getState()==2){
                if (sms.getType()==1){
                    result= every8DSmsUtil.sendSms(sms.getSubject(),sms.getMsg(),sms.getReceive_phone(),"",0);
                }
                if(result!=null && result!=""){
                    String[] strArr = result.split(",");
                    if ("1".equals(strArr[1])) {
                        sms.setUser_id(token.getId());
                        sms.setState(3);
                        sms.setSend_time(new Date());
                        sms.setApi_respond(sms.getApi_respond()+"&&&"+result);
                        if (smsService.updateSmsInfoBySms(sms)>0){
                            map.put("status",0);
                            map.put("tip","success");
                        }else{
                            map.put("status",-11);
                            map.put("tip","重發成功,修改郵件狀態失敗");
                        }
                    }else{
                        map.put("status",-11);
                        map.put("tip","重發失敗,後台接口異常");
                    }
                }else{
                    map.put("status",-11);
                    map.put("tip","重發失敗,請稍候再試");
                }
            }else{
                map.put("status",-7);
                map.put("tip","該郵件不允許重發");
            }

        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
}
