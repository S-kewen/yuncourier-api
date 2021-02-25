package com.boot.yuncourier.controller.service.mail;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.service.mail.Mail;
import com.boot.yuncourier.entity.service.mail.Smtp;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.mail.MailService;
import com.boot.yuncourier.service.service.mail.SmtpService;
import com.boot.yuncourier.service.user.LoginRecordService;
import com.boot.yuncourier.service.user.NewsService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.SmtpMailUtil;
import com.boot.yuncourier.util.TencentCloudCaptchaUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: skwen
 * @Description: MailController-我的郵件
 * @Date: 2020-01-31
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class MailController {
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
    private HttpServletRequest request;
    @Autowired
    private SmtpMailUtil smtpMailUtil;
    @Autowired
    private SmtpService smtpService;
    @LoginToken
    @RequestMapping("listMyMail")
    public Object listMyMail(int pageNumber,int pageSize,String content,int state,int content_type,String startTime,String endTime) throws JSONException, ParseException {
        JSONObject json=new JSONObject();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            User user = new User();
            user.setId(token.getId());
            user.setContent(content);
            user.setState(state);
            user.setType(content_type);
            if(startTime!=null && startTime!=""){
                user.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if(endTime!=null && endTime!=""){
                user.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }
            PageHelper.startPage(pageNumber,pageSize);
            List<Mail> select = mailService.getMailListByUser(user);
            PageInfo<Mail> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteMail")
    public Map<String, Object> deleteMail(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Mail mail = new Mail();
            mail.setUser_id(token.getId());
            mail.setId(id);
            int count=mailService.deleteMailByMail(mail);
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
    @RequestMapping("getMailInfo")
    public Map<String, Object> getMailInfo(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Mail mail = new Mail();
            mail.setUser_id(token.getId());
            mail.setId(id);
            mail=mailService.getMailInfoByMail(mail);
            if (mail!=null){
                map.put("status",0);
                map.put("tip","success");
                map.put("softwareName",mail.getSoftware_name());
                map.put("softwareId",mail.getSoftware_id());
                map.put("token",mail.getToken());
                map.put("content_type",mail.getContent_type());
                map.put("state",mail.getState());
                map.put("receive_mail",mail.getReceive_mail());
                map.put("sender",mail.getSender());
                map.put("title",mail.getTitle());
                map.put("msg",mail.getMsg());
                map.put("ip",mail.getIp());
                map.put("addTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mail.getAdd_time()));
                map.put("remark",mail.getRemark());
            }else{
                map.put("status",-11);
                map.put("tip","該郵件不存在,請稍候再試");
            }

        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
    @LoginToken
    @RequestMapping("retryMail")
    public Map<String, Object> retryMail(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Mail mail = new Mail();
            mail.setUser_id(token.getId());
            mail.setId(id);
            mail=mailService.getMailInfoByMail(mail);
            if (mail==null && mail.getState()!=2){
                map.put("status",-7);
                map.put("tip","該郵件不允許重發");
                return map;
            }
            Smtp smtp = new Smtp();
            smtp.setUser_id(token.getId());
            smtp.setSoftware_id(mail.getSoftware_id());
            List<Smtp> smtpList=smtpService.getSmtpListBySmtp(smtp);
            if(smtpList.size()==0){
                map.put("status",-28);
                map.put("tip","拉取smtp失敗,請添加smtp配置後再試");
                return map;
            }
            Map<String,String> result = new HashMap<>();
            for(int i=0;i<smtpList.size();i++){
                    if (mail.getContent_type()==1){
                        result= smtpMailUtil.sendEasyMail(smtpList.get(i).getHost(),smtpList.get(i).getAccount(),smtpList.get(i).getPassword(),mail.getReceive_mail(),mail.getTitle(),mail.getMsg());
                    }else if(mail.getContent_type()==2){
                        result= smtpMailUtil.sendHtmlMail(smtpList.get(i).getHost(),smtpList.get(i).getAccount(),smtpList.get(i).getPassword(),mail.getReceive_mail(),mail.getTitle(),mail.getMsg());
                    }else if (mail.getContent_type()==3){
                        result= smtpMailUtil.sendYunTechFlowWarnMail(smtpList.get(i).getHost(),smtpList.get(i).getAccount(),smtpList.get(i).getPassword(),mail.getReceive_mail(),mail.getTitle(), util.getUrlValue(mail.getApi_request(),"ip"), util.getUrlValue(mail.getApi_request(),"extUp"), util.getUrlValue(mail.getApi_request(),"extDown"), util.getUrlValue(mail.getApi_request(),"insUp"), util.getUrlValue(mail.getApi_request(),"insDown"), util.getUrlValue(mail.getApi_request(),"flow"), util.getUrlValue(mail.getApi_request(),"ratio"), util.getUrlValue(mail.getApi_request(),"PoliceValue"));
                    }
                if("true".equals(result.get("status"))){
                    mail.setUser_id(token.getId());
                    mail.setState(3);
                    mail.setSender(result.get("sender"));
                    mail.setSend_time(new Date());
                    if (mailService.updateMailInfoByMail(mail)>0){
                        map.put("status",0);
                        map.put("tip","success");
                    }else{
                        map.put("status",-11);
                        map.put("tip","重發成功,修改郵件狀態失敗");
                    }
                    break;
                }else{
                    map.put("status",-11);
                    map.put("tip","重發失敗,請稍候再試");
                }
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
}
