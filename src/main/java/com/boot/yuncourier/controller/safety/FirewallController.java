package com.boot.yuncourier.controller.safety;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.safety.FirewallService;
import com.boot.yuncourier.service.service.link.LinkRecordService;
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
 * @Description: FirewallController-防火墙
 * @Date: 2020-01-30
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class FirewallController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private Util util;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private FirewallService firewallService;
    @Autowired
    private LinkRecordService linkRecordService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SmtpMailUtil smtpMailUtil;
    @LoginToken
    @RequestMapping("listFirewall")
    public Object listFirewall(int pageNumber,int pageSize,String content,int state,int content_type,String startTime,String endTime) throws JSONException, ParseException {
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
            List<Firewall> select = firewallService.getFirewallListByUser(user);
            PageInfo<Firewall> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteFirewall")
    public Map<String, Object> deleteFirewall(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Firewall firewall= new Firewall();
            firewall.setUser_id(token.getId());
            firewall.setId(id);
            int count=firewallService.deleteFirewallByFirewall(firewall);
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
    @RequestMapping("updateFirewall")
    public Map<String, Object> updateFirewall(int id,int softwareId,int object,int type,String ip, String remark) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Firewall firewall = new Firewall();
            firewall.setId(id);
            firewall.setUser_id(token.getId());
            firewall.setSoftware_id(softwareId);
            firewall.setObject(object);
            firewall.setType(type);
            firewall.setIp(ip);
            Firewall flrewall2 = firewallService.getFirewallInfoByIpAndSoftwareId(firewall);
            if(flrewall2!=null && flrewall2.getId()!=id){
                map.put("status",-20);
                map.put("tip","該記錄已存在");
                return map;
            }
            firewall.setRemark(remark);
            int count=firewallService.updateFirewallByFirewall(firewall);
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
    @RequestMapping("addFirewall")
    public Map<String, Object> addFirewall(int softwareId,int object,int type,String ip, String remark) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Software software = new Software();
            software.setUser_id(token.getId());
            software.setId(softwareId);
            software=softwareService.getSoftwareInfoBySoftware(software);
            if (softwareId!=-1){
                if (software==null){
                    map.put("status",-10);
                    map.put("tip","應用不存在");
                    return map;
                }
            }
            Firewall firewall = new Firewall();
            firewall.setUser_id(token.getId());
            firewall.setSoftware_id(softwareId);
            firewall.setObject(object);
            firewall.setType(type);
            firewall.setIp(ip);
            if(firewallService.getFirewallInfoByIpAndSoftwareId(firewall)!=null){
                map.put("status",-20);
                map.put("tip","該記錄已存在");
                return map;
            }
            firewall.setRemark(remark);
            int count=firewallService.addFirewallByFirewall(firewall);
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
    @RequestMapping("changeFirewallState")
    public Map<String, Object> changeFirewallState(int id,int state) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Firewall firewall= new Firewall();
            firewall.setUser_id(token.getId());
            firewall.setId(id);
            firewall.setState(state);
            int count=firewallService.updateFirewallByFirewall(firewall);
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
    @RequestMapping("getFirewallInfo")
    public Map<String, Object> getFirewallInfo(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Firewall firewall= new Firewall();
            firewall.setUser_id(token.getId());
            firewall.setId(id);
            firewall=firewallService.getFirewallInfoByFirewall(firewall);
            if (firewall!=null){
                map.put("status",0);
                map.put("tip","success");
                map.put("softwareId",firewall.getSoftware_id());
                map.put("object",firewall.getObject());
                map.put("type",firewall.getType());
                map.put("ip",firewall.getIp());
                map.put("remark",firewall.getRemark());
            }else{
                map.put("status", -21);
                map.put("tip", "該記錄不存在");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }

}
