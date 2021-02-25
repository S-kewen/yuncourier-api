package com.boot.yuncourier.api;

import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.service.mail.Mail;
import com.boot.yuncourier.entity.service.mail.Smtp;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.safety.FirewallService;
import com.boot.yuncourier.service.safety.InterceptRecordService;
import com.boot.yuncourier.service.service.mail.MailService;
import com.boot.yuncourier.service.service.mail.SmtpService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.SmtpMailUtil;
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
import java.util.List;
import java.util.Map;

/**
 * @Author: skwen
 * @Description: 郵件api
 * @Date: 2020-01-25
 */
@RestController
@RequestMapping("/api")
@Api("郵件api")//描述类/接口的主要用途
@ApiResponses({
        @ApiResponse(code=200,message="ok"),
        @ApiResponse(code=201,message="請求頭接收完畢"),
        @ApiResponse(code=401,message="請求被要求驗證身份"),
        @ApiResponse(code=403,message="服務器拒絕了您的請求"),
        @ApiResponse(code=404,message="請求路徑不對"),
        @ApiResponse(code=500,message="系統內部錯誤")
})
public class MailApi {
    @Value("${mail.easyMail.cost}")
    private double mailEasyMailCost;
    @Value("${mail.htmlMail.cost}")
    private double mailHtmlMailCost;
    @Value("${mail.yuntechFlowWarn.cost}")
    private double mailYuntechFlowWarnMailCost;
    @Autowired
    private Util util;
    @Autowired
    private SmtpMailUtil smtpMailUtil;
    @Autowired
    private MailService mailService;
    @Autowired
    private FirewallService firewallService;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private InterceptRecordService interceptRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private SmtpService smtpService;
    @RequestMapping(value = "easyMail",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="通過應用ID和token發送普通郵件",notes="批量發送請用英文分號隔開輸入多為收件人,費用:2元/條", produces = "application/json")//描述方法用途和注意事項
    @ApiImplicitParams({//描述方法的参数
            @ApiImplicitParam(name = "applyId",value = "應用ID",dataType = "int",paramType="query",required=true, example = "0"),
            @ApiImplicitParam(name = "token",value = "應用token",dataType = "String",paramType="query",required=true, example = "32位token"),
            @ApiImplicitParam(name = "to",value = "收件人",dataType = "String",paramType="query",required=true,example="123456@qq.com"),
            @ApiImplicitParam(name = "title",value = "郵件標題",dataType = "String",paramType="query",required=true,example="這是一個標題"),
            @ApiImplicitParam(name = "msg",value = "郵件內容",dataType = "String",paramType="query",required=true,example="請輸入郵件內容"),
            @ApiImplicitParam(name = "remark",value = "備註",dataType = "String",paramType="query",required=false)
    })
    public Map<String, Object> easyMail(String applyId, String token,
                String to, String title, String msg, String remark) throws IOException, ParseException {
        Map<String,Object> map = new HashMap<>();
        map.put("timestamp",new Date().getTime());
        if(applyId==null || token==null || to==null || title==null || msg==null){
            map.put("status",-1);
            map.put("tip","different parameters");
            return map;
        }
        if(remark==null){
            remark="";
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
        firewall.setObject(2);
        Firewall firewallCheck = firewallService.checkFirewallByFirewall(firewall);
        if(firewallCheck==null) {
            User user = new User();
            user.setId(software.getUser_id());
            user=userService.getById(user);
            if(to.indexOf(";")==-1){
                to+=";";
            }
            String [] tos=to.split(";");
            double cost=mailEasyMailCost*tos.length;
            if (user.getBalance()<cost){
                map.put("status",-5);
                map.put("tip","insufficient balance");
                map.put("balance",user.getBalance());
                map.put("cost",cost);
                return map;
            }
            Mail mail = new Mail();
            mail.setUser_id(software.getUser_id());
            mail.setSoftware_id(software.getId());
            mail.setToken(software.getToken());
            mail.setIp(util.getLocalIp(request));
            mail.setTitle(title);
            mail.setMsg(msg);
            mail.setContent_type(1);
            mail.setSystem(util.getClientSystem(request));
            mail.setBrowser(util.getClientBrowser(request));
            mail.setHeader(request.getHeader("user-agent"));
            mail.setRemark(remark);
            mail.setApi_request(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI() +"?"+request.getQueryString());
            double realCost=0;
            int successAmount=0;
            map.put("balance",user.getBalance());
            map.put("cost",0);
            Smtp smtp = new Smtp();
            smtp.setUser_id(mail.getUser_id());
            smtp.setSoftware_id(software.getId());
            List<Smtp> smtpList=smtpService.getSmtpListBySmtp(smtp);
            if(smtpList.size()==0){
                map.put("status",-6);
                map.put("tip","smtp configuration does not exist");
                return map;
            }
            Map<String,String> result = new HashMap<>();
            for(int i=0;i<tos.length;i++){
                for(int ii=0;ii<smtpList.size();ii++){
                    if(tos[i]!=null && tos[i]!=""){
                        result= smtpMailUtil.sendEasyMail(smtpList.get(ii).getHost(),smtpList.get(ii).getAccount(),smtpList.get(ii).getPassword(),tos[i],title,msg);
                        if (!"true".equals(result.get("status"))){
                            mail.setState(2);
                            mail.setApi_respond(result.get("message"));
                        }else{
                            mail.setState(1);
                            mail.setApi_respond("true");
                            realCost+=mailEasyMailCost;
                            successAmount++;
                            map.put("cost",realCost);
                            map.put("balance",user.getBalance()-realCost);
                            break;
                        }
                    }
                }
                mail.setReceive_mail(tos[i]);
                if(result.get("sender")!=null){
                    mail.setSender(result.get("sender"));
                }else{
                    mail.setSender("");
                }
                mail.setSend_time(util.getNowTimeDate());
                mailService.addMailByMail(mail);
        }
            map.put("amount",tos.length);
            map.put("successAmount",successAmount);
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
            interceptRecord.setObject(2);
            interceptRecord.setSystem(util.getClientSystem(request));
            interceptRecord.setBrowser(util.getClientBrowser(request));
            interceptRecord.setHeader(request.getHeader("user-agent"));
            interceptRecordService.addInterceptRecordByInterceptRecord(interceptRecord);
            map.put("status",-22);
            map.put("tip","firewall blocking");
        }
        return map;
    }

    @RequestMapping(value = "htmlMail",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="通過應用ID和token發送html郵件",notes="批量發送請用英文分號隔開輸入多為收件人,費用:2.5元/條", produces = "application/json")//描述方法用途和注意事項
    @ApiImplicitParams({//描述方法的参数
            @ApiImplicitParam(name = "applyId",value = "應用ID",dataType = "int",paramType="query",required=true, example = "0"),
            @ApiImplicitParam(name = "token",value = "應用token",dataType = "String",paramType="query",required=true, example = "32位token"),
            @ApiImplicitParam(name = "to",value = "收件人",dataType = "String",paramType="query",required=true,example="123456@qq.com"),
            @ApiImplicitParam(name = "title",value = "郵件標題",dataType = "String",paramType="query",required=true,example="這是一個標題"),
            @ApiImplicitParam(name = "msg",value = "郵件內容",dataType = "String",paramType="query",required=true,example="請輸入郵件內容"),
            @ApiImplicitParam(name = "remark",value = "備註",dataType = "String",paramType="query",required=false)
    })
    public Map<String, Object> htmlMail(String applyId, String token,
                           String to, String title, String msg, String remark) throws IOException, ParseException {
        Map<String,Object> map = new HashMap<>();
        map.put("timestamp",new Date().getTime());
        if(applyId==null || token==null || to==null || title==null || msg==null){
            map.put("status",-1);
            map.put("tip","different parameters");
            return map;
        }
        if(remark==null){
            remark="";
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
        firewall.setObject(2);
        Firewall firewallCheck = firewallService.checkFirewallByFirewall(firewall);
        if(firewallCheck==null) {
            User user = new User();
            user.setId(software.getUser_id());
            user=userService.getById(user);
            if(to.indexOf(";")==-1){
                to+=";";
            }
            String [] tos=to.split(";");
            double cost=mailHtmlMailCost*tos.length;
            if (user.getBalance()<cost){
                map.put("status",-5);
                map.put("tip","insufficient balance");
                map.put("balance",user.getBalance());
                map.put("cost",cost);
                return map;
            }
            Mail mail = new Mail();
            mail.setUser_id(software.getUser_id());
            mail.setSoftware_id(software.getId());
            mail.setToken(software.getToken());
            mail.setIp(util.getLocalIp(request));
            mail.setTitle(title);
            mail.setMsg(msg);
            mail.setContent_type(2);
            mail.setSystem(util.getClientSystem(request));
            mail.setBrowser(util.getClientBrowser(request));
            mail.setHeader(request.getHeader("user-agent"));
            mail.setRemark(remark);
            mail.setApi_request(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI() +"?"+request.getQueryString());
            double realCost=0;
            int successAmount=0;
            map.put("balance",user.getBalance());
            map.put("cost",0);
            Smtp smtp = new Smtp();
            smtp.setUser_id(mail.getUser_id());
            smtp.setSoftware_id(software.getId());
            List<Smtp> smtpList=smtpService.getSmtpListBySmtp(smtp);
            if(smtpList.size()==0){
                map.put("status",-6);
                map.put("tip","smtp configuration does not exist");
                return map;
            }
            Map<String,String> result = new HashMap<>();
            for(int i=0;i<tos.length;i++){
                for(int ii=0;ii<smtpList.size();ii++){
                    if(tos[i]!=null && tos[i]!=""){
                        result= smtpMailUtil.sendHtmlMail(smtpList.get(ii).getHost(),smtpList.get(ii).getAccount(),smtpList.get(ii).getPassword(),tos[i],title,msg);
                        if (!"true".equals(result.get("status"))){
                            mail.setState(2);
                            mail.setApi_respond(result.get("message"));
                        }else{
                            mail.setState(1);
                            mail.setApi_respond("true");
                            realCost+=mailHtmlMailCost;
                            successAmount++;
                            map.put("cost",realCost);
                            map.put("balance",user.getBalance()-realCost);
                            break;
                        }
                    }
                }
                mail.setReceive_mail(tos[i]);
                if(result.get("sender")!=null){
                    mail.setSender(result.get("sender"));
                }else{
                    mail.setSender("");
                }
                mail.setSend_time(util.getNowTimeDate());
                mailService.addMailByMail(mail);
            }
            map.put("amount",tos.length);
            map.put("successAmount",successAmount);
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
            interceptRecord.setObject(2);
            interceptRecord.setSystem(util.getClientSystem(request));
            interceptRecord.setBrowser(util.getClientBrowser(request));
            interceptRecord.setHeader(request.getHeader("user-agent"));
            interceptRecordService.addInterceptRecordByInterceptRecord(interceptRecord);
            map.put("status",-22);
            map.put("tip","firewall blocking");
        }
        return map;
    }

    @RequestMapping(value = "yuntechFlowWarn",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="通過應用ID和token發送yunTech流量告警郵件",notes="批量發送請用英文分號隔開輸入多為收件人,費用:3元/條", produces = "application/json")//描述方法用途和注意事項
    @ApiImplicitParams({//描述方法的参数
            @ApiImplicitParam(name = "applyId",value = "應用ID",dataType = "int",paramType="query",required=true, example = "0"),
            @ApiImplicitParam(name = "token",value = "應用token",dataType = "String",paramType="query",required=true, example = "32位token"),
            @ApiImplicitParam(name = "to",value = "收件人",dataType = "String",paramType="query",required=true,example="123456@qq.com"),
            @ApiImplicitParam(name = "title",value = "郵件標題",dataType = "String",paramType="query",required=true,example="這是一個標題"),
            @ApiImplicitParam(name = "ip",value = "告警ip",dataType = "String",paramType="query",required=true,example="127.0.0.1"),
            @ApiImplicitParam(name = "extUp",value = "校外上傳",dataType = "float",paramType="query",required=true,example="1.00"),
            @ApiImplicitParam(name = "extDown",value = "校外下載",dataType = "float",paramType="query",required=true,example="1.00"),
            @ApiImplicitParam(name = "insUp",value = "校內上傳",dataType = "float",paramType="query",required=true,example="1.00"),
            @ApiImplicitParam(name = "insDown",value = "校內下載",dataType = "float",paramType="query",required=true,example="1.00"),
            @ApiImplicitParam(name = "flow",value = "流量總值",dataType = "float",paramType="query",required=true,example="4.00"),
            @ApiImplicitParam(name = "ratio",value = "傳輸比",dataType = "float",paramType="query",required=true,example="1.00"),
            @ApiImplicitParam(name = "warnValue",value = "告警預設值",dataType = "float",paramType="query",required=true,example="4.00"),
            @ApiImplicitParam(name = "remark",value = "備註",dataType = "String",paramType="query",required=false)
    })
    public Map<String, Object> yuntechFlowWarn(String applyId, String token,
                                  String to, String title, String ip, String extUp, String extDown,
                                  String insUp, String insDown, String flow, String ratio,
                                  String warnValue,String remark) throws ParseException {
        Map<String,Object> map = new HashMap<>();
        map.put("timestamp",new Date().getTime());
        if(applyId==null || token==null || to==null || title==null || ip==null || extUp==null || extDown==null
                || insUp==null || insDown==null || flow==null || ratio==null || warnValue==null){
            map.put("status",-1);
            map.put("tip","different parameters");
            return map;
        }
        if(remark==null){
            remark="";
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
        firewall.setObject(2);
        Firewall firewallCheck = firewallService.checkFirewallByFirewall(firewall);
        if(firewallCheck==null) {
            User user = new User();
            user.setId(software.getUser_id());
            user=userService.getById(user);
            if(to.indexOf(";")==-1){
                to+=";";
            }
            String [] tos=to.split(";");
            double cost=mailYuntechFlowWarnMailCost*tos.length;
            if (user.getBalance()<cost){
                map.put("status",-5);
                map.put("tip","insufficient balance");
                map.put("balance",user.getBalance());
                map.put("cost",cost);
                return map;
            }
            Mail mail = new Mail();
            mail.setUser_id(software.getUser_id());
            mail.setSoftware_id(software.getId());
            mail.setToken(software.getToken());
            mail.setIp(util.getLocalIp(request));
            mail.setTitle(title);
            mail.setMsg(request.getQueryString());
            mail.setContent_type(3);
            mail.setSystem(util.getClientSystem(request));
            mail.setBrowser(util.getClientBrowser(request));
            mail.setHeader(request.getHeader("user-agent"));
            mail.setRemark(remark);
            mail.setApi_request(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI() +"?"+request.getQueryString());
            double realCost=0;
            int successAmount=0;
            map.put("balance",user.getBalance());
            map.put("cost",0);
            Smtp smtp = new Smtp();
            smtp.setUser_id(mail.getUser_id());
            smtp.setSoftware_id(software.getId());
            List<Smtp> smtpList=smtpService.getSmtpListBySmtp(smtp);
            if(smtpList.size()==0){
                map.put("status",-6);
                map.put("tip","smtp configuration does not exist");
                return map;
            }
            Map<String,String> result = new HashMap<>();
            for(int i=0;i<tos.length;i++){
                for(int ii=0;ii<smtpList.size();ii++){
                    if(tos[i]!=null && tos[i]!=""){
                        result= smtpMailUtil.sendYunTechFlowWarnMail(smtpList.get(ii).getHost(),smtpList.get(ii).getAccount(),smtpList.get(ii).getPassword(),tos[i],title,ip,extUp,extDown,insUp,insDown,flow,ratio,warnValue);
                        if (!"true".equals(result.get("status"))){
                            mail.setState(2);
                            mail.setApi_respond(result.get("message"));
                        }else{
                            mail.setState(1);
                            mail.setApi_respond("true");
                            realCost+=mailYuntechFlowWarnMailCost;
                            successAmount++;
                            map.put("cost",realCost);
                            map.put("balance",user.getBalance()-realCost);
                            break;
                        }
                    }
                }
                mail.setReceive_mail(tos[i]);
                if(result.get("sender")!=null){
                    mail.setSender(result.get("sender"));
                }else{
                    mail.setSender("");
                }
                mail.setSend_time(util.getNowTimeDate());
                mailService.addMailByMail(mail);
            }
            map.put("amount",tos.length);
            map.put("successAmount",successAmount);
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
            interceptRecord.setSoftware_id(firewallCheck.getSoftware_id());
            interceptRecord.setReal_ip(util.getLocalIp(request));
            interceptRecord.setFirewall_ip(firewallCheck.getIp());
            interceptRecord.setObject(2);
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
