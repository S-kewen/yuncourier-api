package com.boot.yuncourier.api;

import com.alibaba.fastjson.JSONObject;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.service.log.Log;
import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.service.var.VarRecord;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.safety.FirewallService;
import com.boot.yuncourier.service.safety.InterceptRecordService;
import com.boot.yuncourier.service.service.log.LogService;
import com.boot.yuncourier.service.service.var.VarRecordService;
import com.boot.yuncourier.service.service.var.VarService;
import com.boot.yuncourier.util.RSAEncryptUtil;
import com.boot.yuncourier.util.Rc4Util;
import com.boot.yuncourier.util.Util;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: skwen
 * @Description: varApi
 * @Date: 2020-04-29
 */
@RestController
@RequestMapping("/api/log")
@Api("logApi")//描述类/接口的主要用途
@ApiResponses({
        @ApiResponse(code = 200, message = "ok"),
        @ApiResponse(code = 201, message = "請求頭接收完畢"),
        @ApiResponse(code = 401, message = "請求被要求驗證身份"),
        @ApiResponse(code = 403, message = "服務器拒絕了您的請求"),
        @ApiResponse(code = 404, message = "請求路徑不對"),
        @ApiResponse(code = 500, message = "系統內部錯誤")
})
@ApiModel
public class LogApi {
    @Autowired
    private Util util;
    @Autowired
    private LogService logService;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private FirewallService firewallService;
    @Autowired
    private InterceptRecordService interceptRecordService;
    @Autowired
    private Rc4Util rc4Util;
    @Autowired
    HttpServletRequest request;
    @Autowired
    RSAEncryptUtil rsaEncryptUtil;

    @Value("${log.privateKey}")
    private String privateKey;

    @RequestMapping(value = "addLog", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "通過應用ID和token添加日誌記錄", notes = "加密校驗", produces = "application/json")
//描述方法用途和注意事項
    @ApiImplicitParams({//描述方法的参数
            @ApiImplicitParam(name = "id", value = "變量ID", dataType = "int", paramType = "query", required = true, example = "0"),
            @ApiImplicitParam(name = "token", value = "變量token", dataType = "String", paramType = "query", required = true, example = "32位字符串"),
            @ApiImplicitParam(name = "mac", value = "機器碼", dataType = "String", paramType = "query", required = true, example = ""),
            @ApiImplicitParam(name = "title", value = "標題", dataType = "String", paramType = "query", required = true, example = ""),
            @ApiImplicitParam(name = "content", value = "日誌內容", dataType = "String", paramType = "query", required = true, example = "加密后的数据"),
            @ApiImplicitParam(name = "remark", value = "備註", dataType = "String", paramType = "query", required = true, example = ""),
            @ApiImplicitParam(name = "timestamp", value = "13位時間戳", dataType = "String", paramType = "query", required = true, example = ""),
            @ApiImplicitParam(name = "random", value = "隨機16位字符串", dataType = "String", paramType = "query", required = true, example = ""),
            @ApiImplicitParam(name = "sign", value = "加密校驗", dataType = "String", paramType = "query", required = true, example = "MD5(base64(id+MD5(token)+mac+MD5(title+content+remark)+timestamp+random+content))"),
    })
    public Map<String, Object> addLog(int id, String token, String mac, String title, String content, String remark, String timestamp, String random, String sign) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", new Date().getTime());
        if (token == null || timestamp == null || random == null || sign == null) {
            map.put("status", -1);
            map.put("tip", "different parameters");
            return map;
        }
        if (token.length() != 32 || timestamp.length() != 13 || random.length() != 16 || sign.length() != 32) {
            map.put("status", -2);
            map.put("tip", "check error");
            return map;
        }
        //MD5(base64(id+MD5(token)+mac+MD5(title+content+remark)+timestamp+random))
        content = URLDecoder.decode(content, "UTF-8");
        // System.out.println(id + util.getMd5(token) + mac + timestamp + random + "->" + sign);
        if (!util.getMd5(util.StrToBase64(id + util.getMd5(token) + mac + timestamp + random + content)).equals(sign)) {
            map.put("status", -3);
            map.put("tip", "sign error");
            return map;
        }
        Software software = new Software();
        software.setId(id);
        software.setToken(token);
        software = softwareService.checkBySoftware(software);
        if (software != null) {
            Firewall firewall = new Firewall();
            firewall.setUser_id(software.getUser_id());
            firewall.setSoftware_id(software.getId());
            firewall.setIp(util.getLocalIp(request));
            firewall.setObject(8);
            Firewall firewallCheck = firewallService.checkFirewallByFirewall(firewall);
            if (firewallCheck == null) {
                Log log = new Log();
                log.setUser_id(software.getUser_id());
                log.setSoftware_id(software.getId());
                log.setState(1);
                log.setType(1);
                log.setIp(util.getLocalIp(request));
                log.setMac(mac);
                log.setHeader(request.getHeader("user-agent"));
                log.setTitle(title);
                log.setContent(rsaEncryptUtil.decrypt(util.Base64ToStr(content),privateKey));
                log.setRemark(remark);
                int count = logService.insertOne(log);
                if (count > 0) {
                    map.put("status", 0);
                    map.put("tip", "success");
                    return map;
                } else {
                    map.put("status", -5);
                    map.put("tip", "system error");
                    return map;
                }
            } else {
                InterceptRecord interceptRecord = new InterceptRecord();
                interceptRecord.setUser_id(firewallCheck.getUser_id());
                interceptRecord.setSoftware_id(software.getId());
                interceptRecord.setReal_ip(util.getLocalIp(request));
                interceptRecord.setFirewall_ip(firewallCheck.getIp());
                interceptRecord.setObject(8);
                interceptRecord.setSystem(util.getClientSystem(request));
                interceptRecord.setBrowser(util.getClientBrowser(request));
                interceptRecord.setHeader(request.getHeader("user-agent"));
                interceptRecordService.addInterceptRecordByInterceptRecord(interceptRecord);
                map.put("status", -8);
                map.put("tip", "firewall blocking");
                map.put("id", firewallCheck.getId());
            }
        } else {
            map.put("status", -4);
            map.put("tip", "token error");
        }
        return map;
    }
}
