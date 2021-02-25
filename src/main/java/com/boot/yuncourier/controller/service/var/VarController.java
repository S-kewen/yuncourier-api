package com.boot.yuncourier.controller.service.var;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.service.line.LineConfigService;
import com.boot.yuncourier.service.service.var.VarService;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: skwen
 * @ClassName: VarController
 * @Description: Conteoller
 * @Date: 2020-04-29
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class VarController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private VarService varService;
    @Autowired
    private HttpServletRequest request;
    @LoginToken
    @RequestMapping("listVar")
    public Object listVar(int pageNumber,int pageSize,String content,int state,String startTime,String endTime) throws JSONException, ParseException {
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
            List<Var> select = varService.getList(user);
            PageInfo<Var> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteVar")
    public Map<String, Object> deleteVar(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Var var= new Var();
            var.setUser_id(token.getId());
            var.setId(id);
            int count=varService.deleteOne(var);
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
    @RequestMapping("changeVarState")
    public Map<String, Object> changeVarState(int id,int state) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Var var= new Var();
            var.setUser_id(token.getId());
            var.setId(id);
            var.setState(state);
            int count=varService.updateOne(var);
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
    @RequestMapping("getVarInfo")
    public Map<String, Object> getVarInfo(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Var var= new Var();
            var.setUser_id(token.getId());
            var.setId(id);
            var=varService.getInfo(var);
            if (var!=null){
                map.put("status",0);
                map.put("tip","success");
                map.put("softwareId",var.getSoftware_id());
                map.put("state",var.getState());
                map.put("type",var.getType());
                map.put("token",var.getToken());
                map.put("expireTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(var.getExpire_time()));
                map.put("title",var.getTitle());
                map.put("content",var.getContent());
                map.put("remark",var.getRemark());
            }else{
                map.put("status",-7);
                map.put("tip","該變量不存在");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
    @LoginToken
    @RequestMapping("addVar")
    public Map<String, Object> addLineConfig(int softwareId, String expireTime, String title, String content, String remark) throws ParseException, UnsupportedEncodingException {
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
            Var var = new Var();
            var.setUser_id(token.getId());
            var.setSoftware_id(softwareId);
            var.setState(1);
            var.setType(1);
            var.setExpire_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expireTime));
            var.setTitle(title);
            var.setContent(URLDecoder.decode(content, "UTF-8" ));
            var.setRemark(remark);
            int count=varService.insertOne(var);
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
    @RequestMapping("updateVar")
    public Map<String, Object> updateVar(int id,int softwareId, String tokenStr, String expireTime, String title, String content,String remark) throws ParseException, UnsupportedEncodingException {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Var var= new Var();
            var.setId(id);
            var.setUser_id(token.getId());
            var.setSoftware_id(softwareId);
            var.setToken(tokenStr);
            var.setExpire_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expireTime));
            var.setTitle(title);
            var.setContent(URLDecoder.decode(content, "UTF-8" ));
            var.setRemark(remark);
            int count=varService.updateOne(var);
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
}
