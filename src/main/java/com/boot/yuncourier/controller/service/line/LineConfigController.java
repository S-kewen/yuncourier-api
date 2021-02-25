package com.boot.yuncourier.controller.service.line;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.service.file.TencentCos;
import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.service.file.TencentCosService;
import com.boot.yuncourier.service.service.line.LineConfigService;
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
import javax.sound.sampled.Line;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: skwen
 * @ClassName: LineConfigController
 * @Description: Conteoller
 * @Date: 2020-03-21
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class LineConfigController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private Util util;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private LineConfigService lineConfigService;
    @Autowired
    private HttpServletRequest request;
    @LoginToken
    @RequestMapping("listLineConfig")
    public Object listLineConfig(int pageNumber,int pageSize,String content,int state,String startTime,String endTime) throws JSONException, ParseException {
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
            List<LineConfig> select = lineConfigService.getList(user);
            PageInfo<LineConfig> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteLineConfig")
    public Map<String, Object> deleteLineConfig(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            LineConfig lineConfig= new LineConfig();
            lineConfig.setUser_id(token.getId());
            lineConfig.setId(id);
            int count=lineConfigService.deleteOne(lineConfig);
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
    @RequestMapping("changeLineConfigState")
    public Map<String, Object> changeLineConfigState(int id,int state) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            LineConfig lineConfig= new LineConfig();
            lineConfig.setUser_id(token.getId());
            lineConfig.setId(id);
            lineConfig.setState(state);
            int count=lineConfigService.updateOne(lineConfig);
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
    @RequestMapping("getLineConfigInfo")
    public Map<String, Object> getLineConfigInfo(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            LineConfig lineConfig= new LineConfig();
            lineConfig.setUser_id(token.getId());
            lineConfig.setId(id);
            lineConfig=lineConfigService.getInfo(lineConfig);
            if (lineConfig!=null){
                map.put("status",0);
                map.put("tip","success");
                map.put("softwareId",lineConfig.getSoftware_id());
                map.put("lineName",lineConfig.getLine_config_name());
                map.put("channelId",lineConfig.getChannel_id());
                map.put("channelSecret",lineConfig.getChannel_secret());
                map.put("token",lineConfig.getToken());
                map.put("remark",lineConfig.getRemark());
            }else{
                map.put("status",-7);
                map.put("tip","該配置不存在");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
    @LoginToken
    @RequestMapping("addLineConfig")
    public Map<String, Object> addLineConfig(int softwareId, String lineName, String channelId, String channelSecret, String accessToken,String remark) {
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
            User user = new User();
            user.setId(token.getId());
            user=userService.getById(user);
            LineConfig lineConfig = new LineConfig();
            lineConfig.setUser_id(token.getId());
            lineConfig.setLine_config_name(lineName);
            if(lineConfigService.getCount(lineConfig)>0){
                map.put("status",-29);
                map.put("tip","該配置已存在");
                return map;
            }
            lineConfig.setType(1);
            lineConfig.setSoftware_id(softwareId);
            lineConfig.setChannel_id(channelId);
            lineConfig.setChannel_secret(channelSecret);
            lineConfig.setToken(accessToken);
            lineConfig.setRemark(remark);
            int count=lineConfigService.insertOne(lineConfig);
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
    @RequestMapping("updateLineConfig")
    public Map<String, Object> updateLineConfig(int id,int softwareId, String lineName, String channelId, String channelSecret, String accessToken,String remark) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            LineConfig lineConfig= new LineConfig();
            lineConfig.setLine_config_name(lineName);
            lineConfig.setUser_id(token.getId());
            lineConfig.setId(id);
            if(lineConfigService.getCount(lineConfig)>0){
                map.put("status",-29);
                map.put("tip","該配置已存在");
                return map;
            }
            lineConfig.setSoftware_id(softwareId);
            lineConfig.setChannel_id(channelId);
            lineConfig.setChannel_secret(channelSecret);
            lineConfig.setToken(accessToken);
            lineConfig.setRemark(remark);
            int count=lineConfigService.updateOne(lineConfig);
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
