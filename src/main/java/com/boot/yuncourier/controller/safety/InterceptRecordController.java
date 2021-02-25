package com.boot.yuncourier.controller.safety;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.safety.FirewallService;
import com.boot.yuncourier.service.safety.InterceptRecordService;
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
 * @Description: InterceptRecordController-攔截記錄
 * @Date: 2020-01-31
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class InterceptRecordController {
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
    private InterceptRecordService interceptRecordService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SmtpMailUtil smtpMailUtil;
    @LoginToken
    @RequestMapping("listInterceptRecord")
    public Object listInterceptRecord(int pageNumber,int pageSize,String content,int content_type,String startTime,String endTime) throws JSONException, ParseException {
        JSONObject json=new JSONObject();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            User user = new User();
            user.setId(token.getId());
            user.setContent(content);
            user.setType(content_type);
            if(startTime!=null && startTime!=""){
                user.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if(endTime!=null && endTime!=""){
                user.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }
            PageHelper.startPage(pageNumber,pageSize);
            List<InterceptRecord> select = interceptRecordService.getInterceptRecordListByUser(user);
            PageInfo<InterceptRecord> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteInterceptRecord")
    public Map<String, Object> deleteInterceptRecord(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            InterceptRecord interceptRecord= new InterceptRecord();
            interceptRecord.setUser_id(token.getId());
            interceptRecord.setId(id);
            int count=interceptRecordService.deleteInterceptRecordByInterceptRecord(interceptRecord);
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
    @RequestMapping("addWhiteByInterceptRecord")
    public Map<String, Object> addWhiteByInterceptRecord(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            InterceptRecord interceptRecord = new InterceptRecord();
            interceptRecord.setUser_id(token.getId());
            interceptRecord.setId(id);
            interceptRecord=interceptRecordService.getInterceptRecordInfoByInterceptRecord(interceptRecord);
            if(interceptRecord!=null){
                Firewall firewall= new Firewall();
                firewall.setUser_id(token.getId());
                firewall.setSoftware_id(interceptRecord.getSoftware_id());
                firewall.setState(1);
                firewall.setType(1);
                firewall.setObject(interceptRecord.getObject());
                firewall.setIp(interceptRecord.getReal_ip());
                firewall.setRemark("");
                if(firewallService.getFirewallInfoByIpAndSoftwareId(firewall)!=null){
                    map.put("status",-20);
                    map.put("tip","該記錄已存在,請勿重複添加");
                    return map;
                }
                int count=firewallService.addFirewallByFirewall(firewall);
                if (count>0){
                    map.put("status",0);
                    map.put("tip","success");
                }else{
                    map.put("status",-7);
                    map.put("tip","該記錄已存在");
                }
            }else{
                map.put("status", -22);
                map.put("tip", "該記錄不存在");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
}
