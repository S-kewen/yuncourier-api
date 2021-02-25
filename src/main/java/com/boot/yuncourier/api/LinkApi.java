package com.boot.yuncourier.api;

import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.service.link.Link;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.safety.FirewallService;
import com.boot.yuncourier.service.safety.InterceptRecordService;
import com.boot.yuncourier.service.service.link.LinkService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.Util;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: skwen
 * @Description: 短連api
 * @Date: 2020-02-14
 */
@RestController
@RequestMapping("/api")
@Api("短連api")//描述类/接口的主要用途
@ApiResponses({
        @ApiResponse(code=200,message="ok"),
        @ApiResponse(code=201,message="請求頭接收完畢"),
        @ApiResponse(code=401,message="請求被要求驗證身份"),
        @ApiResponse(code=403,message="服務器拒絕了您的請求"),
        @ApiResponse(code=404,message="請求路徑不對"),
        @ApiResponse(code=500,message="系統內部錯誤")
})
public class LinkApi {
    @Value("${config.shortHost}")
    private String shortHost;
    @Value("${link.addShort.cost}")
    private double linkAddShortCost;
    @Autowired
    private Util util;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirewallService firewallService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private InterceptRecordService interceptRecordService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "createShort",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="通過應用ID和token生成短連",notes="長鏈需要進行utf-8編碼,費用:2元/條", produces = "application/json")//描述方法用途和注意事項
    @ApiImplicitParams({//描述方法的参数
    @ApiImplicitParam(name = "applyId",value = "應用ID",dataType = "int",paramType="query",required=true, example = "0"),
    @ApiImplicitParam(name = "token",value = "應用token",dataType = "String",paramType="query",required=true, example = "32位token"),
    @ApiImplicitParam(name = "longUrl",value = "長鏈",dataType = "String",paramType="query",required=true,example="https://www.baidu.com/"),
    @ApiImplicitParam(name = "remark",value = "備註",dataType = "String",paramType="query",required=false)
    })
    public Map<String, Object> createShort(String applyId, String token, String longUrl, String remark) throws UnsupportedEncodingException {
        Map<String,Object> map = new HashMap<>();
        map.put("timestamp",new Date().getTime());
            if(applyId==null || token==null || longUrl==null){
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
        firewall.setObject(4);
        Firewall firewallCheck = firewallService.checkFirewallByFirewall(firewall);
        if(firewallCheck==null) {
            User user = new User();
            user.setId(software.getUser_id());
            user=userService.getById(user);
            Link link = new Link();
            longUrl=URLDecoder.decode(longUrl, "utf-8");
            link.setUser_id(user.getId());
            link.setLong_url(longUrl);
            link.setSystem(util.getClientSystem(request));
            link.setBrowser(util.getClientBrowser(request));
            link.setHeader(request.getHeader("user-agent"));
            Link oldLink=linkService.getLinkInfoByLongUrl(link);
            if(oldLink!=null){
                map.put("status",0);
                map.put("tip","the link already exists");
                map.put("short",shortHost+oldLink.getShort_url());
                map.put("cost",0);
                map.put("balance",user.getBalance());
                return map;
            }
            if (user.getBalance()<linkAddShortCost){
                map.put("status",-5);
                map.put("tip","insufficient balance");
                map.put("balance",user.getBalance());
                return map;
            }
            link.setIp(util.getLocalIp(request));
            link.setSoftware_id(Integer.parseInt(applyId));
            link.setToken(token);
            link.setShort_url(util.getRandomStr(2,6,""));
            link.setRemark(remark);
            int count=linkService.addLinkByLink(link);
            if (count>0){
                map.put("status",0);
                map.put("tip","success");
                map.put("short",shortHost+link.getShort_url());
                map.put("balance",user.getBalance());
                map.put("cost",linkAddShortCost);
            }else{
                map.put("status",-6);
                map.put("tip","create failed");
                map.put("balance",user.getBalance()-linkAddShortCost);
                map.put("cost",0);
            }
        }else{
            InterceptRecord interceptRecord = new InterceptRecord();
            interceptRecord.setUser_id(firewallCheck.getUser_id());
            interceptRecord.setSoftware_id(software.getId());
            interceptRecord.setReal_ip(util.getLocalIp(request));
            interceptRecord.setFirewall_ip(firewallCheck.getIp());
            interceptRecord.setObject(4);
            interceptRecord.setSystem(util.getClientSystem(request));
            interceptRecord.setBrowser(util.getClientBrowser(request));
            interceptRecord.setHeader(request.getHeader("user-agent"));
            interceptRecordService.addInterceptRecordByInterceptRecord(interceptRecord);
            map.put("status",-22);
            map.put("tip","firewall blocking");
        }
        return map;
    }

    @RequestMapping(value = "deleteShort",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="通過應用ID和token刪除短連",notes="短連key為短連後6位隨機碼,費用:免費")//描述方法用途和注意事項
    @ApiImplicitParams({//描述方法的参数
            @ApiImplicitParam(name = "applyId",value = "應用ID",dataType = "int",paramType="query",required=true, example = "0"),
            @ApiImplicitParam(name = "token",value = "應用token",dataType = "String",paramType="query",required=true, example = "32位token"),
            @ApiImplicitParam(name = "shortUrlKey",value = "短連key",dataType = "String",paramType="query",required=true, example = "6位隨機碼"),
    })
    public Map<String, Object> htmlMail(String applyId, String token, String shortUrlKey) {
        Map<String,Object> map = new HashMap<>();
        map.put("timestamp",new Date().getTime());
        if(applyId==null || token==null || shortUrlKey==null){
            map.put("status",-1);
            map.put("tip","different parameters");
            return map;
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
        firewall.setObject(4);
        Firewall firewallCheck = firewallService.checkFirewallByFirewall(firewall);
        if(firewallCheck==null) {
            User user = new User();
            user.setId(software.getUser_id());
            user=userService.getById(user);
            Link link = new Link();
            link.setUser_id(user.getId());
            link.setShort_url(shortUrlKey);
            int count=linkService.deleteLinkByShortUrl(link);
            if (count>0){
                map.put("status",0);
                map.put("tip","success");
            }else{
                map.put("status",-7);
                map.put("tip","the link does not exist");
            }
        }else{
            InterceptRecord interceptRecord = new InterceptRecord();
            interceptRecord.setUser_id(firewallCheck.getUser_id());
            interceptRecord.setSoftware_id(software.getId());
            interceptRecord.setReal_ip(util.getLocalIp(request));
            interceptRecord.setFirewall_ip(firewallCheck.getIp());
            interceptRecord.setObject(4);
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
