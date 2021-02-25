package com.boot.yuncourier.api;

import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.service.line.PushMessage;
import com.boot.yuncourier.entity.service.sms.Sms;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.safety.FirewallService;
import com.boot.yuncourier.service.safety.InterceptRecordService;
import com.boot.yuncourier.service.service.line.LineConfigService;
import com.boot.yuncourier.service.service.line.PushMessageService;
import com.boot.yuncourier.service.service.sms.SmsService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.Every8dSmsUtil;
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
 * @Description: lineApi
 * @Date: 2020-03-21
 */
@RestController
@RequestMapping("/api")
@Api("lineApi")//描述类/接口的主要用途
@ApiResponses({
        @ApiResponse(code = 200, message = "ok"),
        @ApiResponse(code = 201, message = "請求頭接收完畢"),
        @ApiResponse(code = 401, message = "請求被要求驗證身份"),
        @ApiResponse(code = 403, message = "服務器拒絕了您的請求"),
        @ApiResponse(code = 404, message = "請求路徑不對"),
        @ApiResponse(code = 500, message = "系統內部錯誤")
})
@ApiModel
public class LineApi {
    @Value("${line.messagingApi.cost}")
    private double messagingApiCost;
    @Autowired
    private Util util;
    @Autowired
    private PushMessageService pushMessageService;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirewallService firewallService;
    @Autowired
    private InterceptRecordService interceptRecordService;
    @Autowired
    private LineUtil lineUtil;
    @Autowired
    private LineConfigService lineConfigService;
    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "pushMessage", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "通過應用ID和token和lineConfigId推送消息", notes = "費用:0.5元/條", produces = "application/json")
//描述方法用途和注意事項
    @ApiImplicitParams({//描述方法的参数
            @ApiImplicitParam(name = "applyId", value = "應用ID", dataType = "int", paramType = "query", required = true, example = "0"),
            @ApiImplicitParam(name = "token", value = "應用token", dataType = "String", paramType = "query", required = true, example = "32位token"),
            @ApiImplicitParam(name = "lineConfigId", value = "line配置ID", dataType = "int", paramType = "query", required = true, example = "0"),
            @ApiImplicitParam(name = "to", value = "userId", dataType = "String", paramType = "query", required = true, example = "通過webhook取得"),
            @ApiImplicitParam(name = "type", value = "消息類型", dataType = "String", paramType = "query", required = true, example = "text"),
            @ApiImplicitParam(name = "text", value = "消息內容", dataType = "String", paramType = "query", required = true, example = "推送內容"),
            @ApiImplicitParam(name = "remark", value = "備註", dataType = "String", paramType = "query", required = false)
    })
    public Map<String, Object> pushMessage(String applyId, String token,
                                           String lineConfigId, String to, String type, String text, String remark) throws IOException, ParseException {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", new Date().getTime());
        if (applyId == null || token == null || lineConfigId == null || to == null || type == null || text == null) {
            map.put("status", -1);
            map.put("tip", "different parameters");
            return map;
        }
        if (remark == null) {
            remark = "";
        }
        Software software = new Software();
        software.setId(Integer.parseInt(applyId));
        software.setToken(token);
        software = softwareService.checkBySoftware(software);
        if (software == null) {
            map.put("status", -2);
            map.put("tip", "required parameter error");
            return map;
        }
        LineConfig lineConfig = new LineConfig();
        lineConfig.setUser_id(software.getUser_id());
        lineConfig.setId(Integer.parseInt(lineConfigId));
        lineConfig = lineConfigService.getInfo(lineConfig);
        if (lineConfig == null) {
            map.put("status", -3);
            map.put("tip", "line config not exist");
            return map;
        }
        if (lineConfig.getSoftware_id() != software.getId()) {
            map.put("status", -4);
            map.put("tip", "line config and software don't match");
            return map;
        }
        if (lineConfig.getState()!=1) {
            map.put("status", -8);
            map.put("tip", "line config stopped");
            return map;
        }
        Firewall firewall = new Firewall();
        firewall.setUser_id(software.getUser_id());
        firewall.setSoftware_id(software.getId());
        firewall.setIp(util.getLocalIp(request));
        firewall.setObject(6);
        Firewall firewallCheck = firewallService.checkFirewallByFirewall(firewall);
        if (firewallCheck == null) {
            User user = new User();
            user.setId(software.getUser_id());
            user = userService.getById(user);
            if (to.indexOf(";") == -1) {
                to += ";";
            }
            String[] tos = to.split(";");
            double cost = messagingApiCost * tos.length;
            if (user.getBalance() < cost) {
                map.put("status", -5);
                map.put("tip", "insufficient balance");
                map.put("balance", user.getBalance());
                return map;
            }
            PushMessage pushMessage = new PushMessage();
            pushMessage.setUser_id(software.getUser_id());
            pushMessage.setSoftware_id(software.getId());
            pushMessage.setToken(token);
            pushMessage.setLine_config_id(Integer.parseInt(lineConfigId));
            pushMessage.setType(type);
            pushMessage.setText(text);
            pushMessage.setIp(util.getLocalIp(request));
            pushMessage.setSystem(util.getClientSystem(request));
            pushMessage.setBrower(util.getClientBrowser(request));
            pushMessage.setHeader(request.getHeader("user-agent"));
            pushMessage.setRemark(remark);
            pushMessage.setApi_request(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI() + "?" + request.getQueryString());
            double realCost = 0;
            int successAmount = 0;
            String result = "";
            for (int i = 0; i < tos.length; i++) {
                if (tos[i] != null && tos[i] != "") {
                    result = lineUtil.pushMessage(tos[i], type, text, lineConfig.getToken());
                    if ("{}".equals(result)) {
                        pushMessage.setState(1);
                        realCost += messagingApiCost;
                        successAmount++;
                    } else {
                        pushMessage.setState(2);
                    }
                } else {
                    pushMessage.setState(2);
                }
                pushMessage.setApi_response(result);
                pushMessage.setTo(tos[i]);
                pushMessage.setApi_request(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI() + "?" + request.getQueryString());
                pushMessageService.insertOne(pushMessage);
            }
            map.put("amount",tos.length);
            map.put("successAmount",successAmount);
            map.put("cost",realCost);
            map.put("balance",user.getBalance()-realCost);
            if (successAmount>0){
                map.put("status",0);
                map.put("tip","success");
                map.put("result",result);
            }else{
                map.put("status",-6);
                map.put("tip","send failed");
                map.put("result",result);
            }
        } else {
            InterceptRecord interceptRecord = new InterceptRecord();
            interceptRecord.setUser_id(firewallCheck.getUser_id());
            interceptRecord.setSoftware_id(software.getId());
            interceptRecord.setReal_ip(util.getLocalIp(request));
            interceptRecord.setFirewall_ip(firewallCheck.getIp());
            interceptRecord.setObject(3);
            interceptRecord.setSystem(util.getClientSystem(request));
            interceptRecord.setBrowser(util.getClientBrowser(request));
            interceptRecord.setHeader(request.getHeader("user-agent"));
            interceptRecordService.addInterceptRecordByInterceptRecord(interceptRecord);
            map.put("status", -22);
            map.put("tip", "firewall blocking");
        }
        return map;
    }
}
