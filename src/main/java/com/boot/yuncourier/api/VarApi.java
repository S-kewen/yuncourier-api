package com.boot.yuncourier.api;

import com.alibaba.fastjson.JSONObject;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.service.line.PushMessage;
import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.service.var.VarRecord;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.safety.FirewallService;
import com.boot.yuncourier.service.safety.InterceptRecordService;
import com.boot.yuncourier.service.service.line.LineConfigService;
import com.boot.yuncourier.service.service.line.PushMessageService;
import com.boot.yuncourier.service.service.var.VarRecordService;
import com.boot.yuncourier.service.service.var.VarService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.LineUtil;
import com.boot.yuncourier.util.Util;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: skwen
 * @Description: varApi
 * @Date: 2020-04-29
 */
@RestController
@RequestMapping("/api/var")
@Api("varApi")//描述类/接口的主要用途
@ApiResponses({
        @ApiResponse(code = 200, message = "ok"),
        @ApiResponse(code = 201, message = "請求頭接收完畢"),
        @ApiResponse(code = 401, message = "請求被要求驗證身份"),
        @ApiResponse(code = 403, message = "服務器拒絕了您的請求"),
        @ApiResponse(code = 404, message = "請求路徑不對"),
        @ApiResponse(code = 500, message = "系統內部錯誤")
})
@ApiModel
public class VarApi {
    @Autowired
    private Util util;
    @Autowired
    private VarService varService;
    @Autowired
    private VarRecordService varRecordService;
    @Autowired
    private FirewallService firewallService;
    @Autowired
    private InterceptRecordService interceptRecordService;
    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "getVar", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "通過應用ID和token獲取遠程變量", notes = "加密校驗", produces = "application/json")
//描述方法用途和注意事項
    @ApiImplicitParams({//描述方法的参数
            @ApiImplicitParam(name = "id", value = "變量ID", dataType = "int", paramType = "query", required = true, example = "0"),
            @ApiImplicitParam(name = "token", value = "變量token", dataType = "String", paramType = "query", required = true, example = "32位字符串"),
            @ApiImplicitParam(name = "timestamp", value = "13位時間戳", dataType = "String", paramType = "query", required = true, example = ""),
            @ApiImplicitParam(name = "random", value = "隨機16位字符串", dataType = "String", paramType = "query", required = true, example = ""),
            @ApiImplicitParam(name = "sign", value = "加密校驗", dataType = "String", paramType = "query", required = true, example = "MD5(base64(id+MD5(token)+timestamp+random))"),
    })
    public Map<String, Object> pushMessage(String id, String token, String timestamp, String random, String sign,String mac) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", new Date().getTime());
        if (id == null || token == null || timestamp == null || random == null || sign == null) {
            map.put("status", -1);
            map.put("tip", "different parameters");
            return map;
        }
        if (token.length() != 32 || timestamp.length() != 13 || random.length() != 16 || sign.length() != 32 || id == "") {
            map.put("status", -2);
            map.put("tip", "check error");
            return map;
        }
        Var var = new Var();
        var.setId(Integer.parseInt(id));
        var = varService.getById(var);
        if (var == null) {
            if (!util.getMd5(util.StrToBase64(id + util.getMd5(token) + timestamp + random)).equals(sign)) {
                map.put("status", -3);
                map.put("tip", "sign error");
                return map;
            }
            map.put("status", -5);
            map.put("tip", "not exist");
            return map;
        }
        VarRecord varRecord = new VarRecord();
        varRecord.setUser_id(var.getUser_id());
        varRecord.setSoftware_id(var.getSoftware_id());
        varRecord.setVar_id(var.getId());
        varRecord.setType(1);
        varRecord.setIp(util.getLocalIp(request));
        varRecord.setHeader(request.getHeader("user-agent"));
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("token", token);
        json.put("timestamp", timestamp);
        json.put("random", random);
        json.put("sigh", sign);
        json.put("mac",mac);
        varRecord.setRequest(json.toString());
        if (!util.getMd5(util.StrToBase64(id + util.getMd5(token) + timestamp + random)).equals(sign)) {
            map.put("status", -3);
            map.put("tip", "sign error");
            varRecord.setState(-3);
            varRecord.setRemark("sign error");
            varRecordService.insertOne(varRecord);
            return map;
        }
        /*if (System.currentTimeMillis() - Long.parseLong(timestamp) >= 6000) {
            map.put("status", -4);
            map.put("tip", "invalid request");
            varRecord.setState(-4);
            varRecord.setRemark("invalid request");
            varRecordService.insertOne(varRecord);
            return map;
        }*/
        if (!var.getToken().equals(token)) {
            map.put("status", -9);
            map.put("tip", "invalid token");
            varRecord.setState(-9);
            varRecord.setRemark("invalid token");
            varRecordService.insertOne(varRecord);
            return map;
        }
        if (var.isDeleted()) {
            map.put("status", -5);
            map.put("tip", "delted");
            varRecord.setState(-5);
            varRecord.setRemark("deleted");
            varRecordService.insertOne(varRecord);
            return map;
        }
        if (var.getState() != 1) {
            map.put("status", -6);
            map.put("tip", "disable");
            varRecord.setState(-6);
            varRecord.setRemark("disable");
            varRecordService.insertOne(varRecord);
            return map;
        }
        if (new Date().after(var.getExpire_time())) {
            map.put("status", -7);
            map.put("tip", "expired");
            varRecord.setState(-7);
            varRecord.setRemark("expired");
            varRecordService.insertOne(varRecord);
            return map;
        }
        Firewall firewall = new Firewall();
        firewall.setUser_id(var.getUser_id());
        firewall.setSoftware_id(var.getSoftware_id());
        firewall.setIp(util.getLocalIp(request));
        firewall.setObject(7);
        Firewall firewallCheck = firewallService.checkFirewallByFirewall(firewall);
        if (firewallCheck == null) {
            map.put("status", 0);
            map.put("tip", "success");
            map.put("title", util.StrToBase64(util.getRandomStr(2,32,"") + util.StrToBase64(var.getTitle())));
            map.put("content", util.StrToBase64(util.getRandomStr(2,32,"") + util.StrToBase64(var.getContent())));
            map.put("sign", util.getMd5(util.StrToBase64(sign)));
            varRecord.setState(1);
            varRecord.setRemark("success");
            varRecordService.insertOne(varRecord);
            return map;
        } else {
            InterceptRecord interceptRecord = new InterceptRecord();
            interceptRecord.setUser_id(firewallCheck.getUser_id());
            interceptRecord.setSoftware_id(var.getSoftware_id());
            interceptRecord.setReal_ip(util.getLocalIp(request));
            interceptRecord.setFirewall_ip(firewallCheck.getIp());
            interceptRecord.setObject(7);
            interceptRecord.setSystem(util.getClientSystem(request));
            interceptRecord.setBrowser(util.getClientBrowser(request));
            interceptRecord.setHeader(request.getHeader("user-agent"));
            interceptRecordService.addInterceptRecordByInterceptRecord(interceptRecord);
            map.put("status", -8);
            map.put("tip", "firewall blocking");
            map.put("id", firewallCheck.getId());
            varRecord.setState(-8);
            varRecord.setRemark("firewall blocking");
            varRecordService.insertOne(varRecord);
        }
        return map;
    }
}
