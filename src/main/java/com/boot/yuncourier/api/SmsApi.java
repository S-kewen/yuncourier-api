package com.boot.yuncourier.api;

import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.service.sms.Sms;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.safety.FirewallService;
import com.boot.yuncourier.service.safety.InterceptRecordService;
import com.boot.yuncourier.service.service.sms.SmsService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.Every8dSmsUtil;
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
 * @Description: 短信api
 * @Date: 2020-01-25
 */
@RestController
@RequestMapping("/api")
@Api("短信api")//描述类/接口的主要用途
@ApiResponses({
        @ApiResponse(code=200,message="ok"),
        @ApiResponse(code=201,message="請求頭接收完畢"),
        @ApiResponse(code=401,message="請求被要求驗證身份"),
        @ApiResponse(code=403,message="服務器拒絕了您的請求"),
        @ApiResponse(code=404,message="請求路徑不對"),
        @ApiResponse(code=500,message="系統內部錯誤")
})
@ApiModel
public class SmsApi {
    @Value("${sms.toTaiwan.cost}")
    private double smsToTaiwanCost;
    @Autowired
    private Util util;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirewallService firewallService;
    @Autowired
    private InterceptRecordService interceptRecordService;
    @Autowired
    private Every8dSmsUtil every8DSmsUtil;
    @Autowired
    HttpServletRequest request;
    @RequestMapping(value = "smsToTaiwan",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="通過應用ID和token發送短信(台灣)",notes="批量發送請用英文分號隔開輸入多為收件人,費用:5元/條", produces = "application/json")//描述方法用途和注意事項
    @ApiImplicitParams({//描述方法的参数
            @ApiImplicitParam(name = "applyId",value = "應用ID",dataType = "int",paramType="query",required=true, example = "0"),
            @ApiImplicitParam(name = "token",value = "應用token",dataType = "String",paramType="query",required=true, example = "32位token"),
            @ApiImplicitParam(name = "to",value = "手機號碼",dataType = "String",paramType="query",required=true,example="0974123456"),
            @ApiImplicitParam(name = "subject",value = "短信主題",dataType = "String",paramType="query",required=false,example="僅用於後台記錄,不會發往對方手機"),
            @ApiImplicitParam(name = "msg",value = "短信內容",dataType = "String",paramType="query",required=true,example="請輸入短信內容(單條限長333字符,超出按多條計費)"),
            @ApiImplicitParam(name = "remark",value = "備註",dataType = "String",paramType="query",required=false)
    })
    public Map<String, Object> smsToTaiwan(String applyId, String token,
                                           String subject, String to, String msg, String remark) throws IOException, ParseException {
        Map<String,Object> map = new HashMap<>();
        map.put("timestamp",new Date().getTime());
        if(applyId==null || token==null || to==null  || msg==null){
            map.put("status",-1);
            map.put("tip","different parameters");
            return map;
        }
        if(remark==null){
            remark="";
        }
        if (subject==null){
            subject="";
        }
        Software software = new Software();
        software.setId(Integer.parseInt(applyId));
        software.setToken(token);
        software=softwareService.checkBySoftware(software);
        if (software==null){
            map.put("status",-1);
            map.put("tip","required parameter error");
            return map;
        }
        Firewall firewall = new Firewall();
        firewall.setUser_id(software.getUser_id());
        firewall.setSoftware_id(software.getId());
        firewall.setIp(util.getLocalIp(request));
        firewall.setObject(3);
        Firewall firewallCheck = firewallService.checkFirewallByFirewall(firewall);
        if(firewallCheck==null) {
            User user = new User();
            user.setId(software.getUser_id());
            user=userService.getById(user);
            int smsNum=1+(msg.length()/333);
            if(to.indexOf(";")==-1){
                to+=";";
            }
            String [] tos=to.split(";");
            double cost = smsToTaiwanCost*smsNum*tos.length;
            if (user.getBalance()<cost){
                map.put("status",-5);
                map.put("tip","insufficient balance");
                map.put("balance",user.getBalance());
                return map;
            }
            Sms sms = new Sms();
            sms.setUser_id(software.getUser_id());
            sms.setSoftware_id(software.getId());
            sms.setToken(software.getToken());
            sms.setType(1);
            sms.setIp(util.getLocalIp(request));
            sms.setSubject(subject);
            sms.setMsg(msg);
            sms.setSystem(util.getClientSystem(request));
            sms.setBrowser(util.getClientBrowser(request));
            sms.setHeader(request.getHeader("user-agent"));
            sms.setRemark(remark);
            double realCost=0;
            int successAmount=0;
            for(int i=0;i<tos.length;i++){
                if(tos[i]!=null && tos[i]!=""){
                String result= every8DSmsUtil.sendSms(subject,msg,tos[i],"",0);
                if (result==""){
                    sms.setState(2);
                }else{
                    String[] strArr = result.split(",");
                    if(strArr[1]!=null && strArr[1]!=""){
                        if(Integer.parseInt(strArr[1])>0){
                            sms.setState(1);
                            sms.setCost((float) (smsNum*smsToTaiwanCost));
                            realCost+=smsNum*smsToTaiwanCost;
                            successAmount++;
                        }else{
                            sms.setState(2);
                            sms.setCost(0);
                        }
                    }else{
                        sms.setState(2);
                        sms.setCost(0);
                    }
                }
                sms.setApi_respond(result);
                sms.setReceive_phone(tos[i]);
                sms.setSend_time(util.getNowTimeDate());
                sms.setApi_request(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI() +"?"+request.getQueryString());
                smsService.addSmsBySms(sms);
            }
            }
            map.put("amount",tos.length);
            map.put("successAmount",successAmount);
            map.put("cost",realCost);
            map.put("balance",user.getBalance()-realCost);
            if (successAmount>0){
                map.put("status",0);
                map.put("tip","success");
            }else{
                map.put("status",-2);
                map.put("tip","send failed");
            }
        }else{
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
            map.put("status",-22);
            map.put("tip","firewall blocking");
        }
        return map;
    }
}
