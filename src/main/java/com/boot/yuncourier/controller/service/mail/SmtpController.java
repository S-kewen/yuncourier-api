package com.boot.yuncourier.controller.service.mail;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.service.mail.Smtp;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.service.mail.SmtpService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.SmtpMailUtil;
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
 * @Description: SmtpController-smtp
 * @Date: 2020-02-18
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class SmtpController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private Util util;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private SmtpService smtpService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SmtpMailUtil smtpMailUtil;
    @LoginToken
    @RequestMapping("listMySmtp")
    public Object listMySmtp(int pageNumber,int pageSize,String content,int state,String startTime,String endTime) throws JSONException, ParseException {
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
            List<Smtp> select = smtpService.getSmtpListByUser(user);
            PageInfo<Smtp> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteSmtp")
    public Map<String, Object> deleteSmtp(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Smtp smtp= new Smtp();
            smtp.setUser_id(token.getId());
            smtp.setId(id);
            int count=smtpService.deleteSmtpBySmtp(smtp);
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
    @RequestMapping("changeSmtpState")
    public Map<String, Object> changeSmtpState(int id,int state) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Smtp smtp= new Smtp();
            smtp.setUser_id(token.getId());
            smtp.setId(id);
            smtp.setState(state);
            int count=smtpService.updateSmtpBySmtp(smtp);
            if (count>0){
                map.put("status",0);
                map.put("tip","success");
            }else{
                map.put("status",-7);
                map.put("tip","修改失敗,請稍候再試");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
    @LoginToken
    @RequestMapping("addSmtp")
    public Map<String, Object> addSmtp(int softwareId,String host, String account, String password,int priority, String remark) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            if(softwareId!=-1){
                Software software = new Software();
                software.setUser_id(token.getId());
                software.setId(softwareId);
                software=softwareService.getSoftwareInfoBySoftware(software);
                if (software==null){
                    map.put("status",-10);
                    map.put("tip","應用不存在");
                    return map;
                }
            }
            Smtp smtp = new Smtp();
            smtp.setUser_id(token.getId());
            smtp.setSoftware_id(softwareId);
            smtp.setHost(host);
            smtp.setAccount(account);
            if(smtpService.getSmtpCount(smtp)>0){
                map.put("status",-23);
                map.put("tip","該Smtp配置已存在");
                return map;
            }
            smtp.setType(2);
            smtp.setPassword(password);
            smtp.setPort(-1);
            smtp.setSsl(false);
            smtp.setPriority(priority);
            smtp.setRemark(remark);
            int count=smtpService.addSmtpBySmtp(smtp);
            if (count>0){
                map.put("status",0);
                map.put("tip","success");
            }else{
                map.put("status",-18);
                map.put("tip","添加失敗,請稍後再試");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
    @LoginToken
    @RequestMapping("getSmtpInfo")
    public Map<String, Object> getSmtpInfo(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Smtp smtp= new Smtp();
            smtp.setUser_id(token.getId());
            smtp.setId(id);
            smtp.setType(2);
            smtp=smtpService.getSmtpInfoBySmtp(smtp);
            if (smtp!=null){
                map.put("status",0);
                map.put("tip","success");
                map.put("softwareId",smtp.getSoftware_id());
                map.put("host",smtp.getHost());
                map.put("account",smtp.getAccount());
                map.put("password",smtp.getPassword());
                map.put("priority",smtp.getPriority());
                map.put("remark",smtp.getRemark());
            }else{
                map.put("status",-24);
                map.put("tip","該Smtp不存在");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
    @LoginToken
    @RequestMapping("updateSmtp")
    public Map<String, Object> updateSmtp(int id,int softwareId,String host,String account,String password,int priority,String remark) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Smtp smtp= new Smtp();
            smtp.setType(2);
            smtp.setUser_id(token.getId());
            smtp.setId(id);
            smtp.setSoftware_id(softwareId);
            smtp.setHost(host);
            smtp.setAccount(account);
            if(smtpService.getSmtpCount(smtp)>1){
                map.put("status",-23);
                map.put("tip","該Smtp配置已存在");
                return map;
            }
            smtp.setPassword(password);
            smtp.setPriority(priority);
            smtp.setRemark(remark);
            int count=smtpService.updateSmtpBySmtp(smtp);
            if (count>0){
                map.put("status",0);
                map.put("tip","success");
            }else{
                map.put("status",-25);
                map.put("tip","修改失敗,請稍後再試");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
    @LoginToken
    @RequestMapping("checkSmtpById")
    public Map<String, Object> checkSmtpById(int id,String email) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Smtp smtp= new Smtp();
            smtp.setUser_id(token.getId());
            smtp.setId(id);
            smtp=smtpService.getSmtpInfoBySmtp(smtp);
            if (smtp!=null){
                if(email!=null && email!=""){
                    Map<String,String> result= smtpMailUtil.sendEasyMail(smtp.getHost(),smtp.getAccount(),smtp.getPassword(),email,"smtp通信測試", util.getRandomStr(2,32,""));
                    if("true".equals(result.get("status"))){
                        map.put("status",0);
                        map.put("tip","success");
                    }else{
                        map.put("status",-27);
                        map.put("tip","通信失敗,請稍後再試");
                        if(result.get("message")==""){
                            map.put("message","time out");
                        }else{
                            map.put("message",result.get("message"));
                        }
                    }
                }else{
                    map.put("status",-26);
                    map.put("tip","郵箱有誤");
                }
            }else{
                map.put("status",-24);
                map.put("tip","該smtp配置不存在");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
}
