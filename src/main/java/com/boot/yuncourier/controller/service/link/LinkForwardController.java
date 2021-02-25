package com.boot.yuncourier.controller.service.link;

import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.service.link.Link;
import com.boot.yuncourier.entity.service.link.LinkRecord;
import com.boot.yuncourier.service.safety.FirewallService;
import com.boot.yuncourier.service.safety.InterceptRecordService;
import com.boot.yuncourier.service.service.link.LinkRecordService;
import com.boot.yuncourier.service.service.link.LinkService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.SmtpMailUtil;
import com.boot.yuncourier.util.TokenUtil;
import com.boot.yuncourier.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @Author: skwen
 * @Description: LinkForwardController-短連攔截轉發
 * @Date: 2020-01-31
 */
@Controller
@RequestMapping("/")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class LinkForwardController {
    @Value("${config.shortHost}")
    private String shortHost;
    @Value("${views.host}")
    private String viewsHost;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private Util util;
    @Autowired
    private InterceptRecordService interceptRecordService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private FirewallService firewallService;
    @Autowired
    private LinkRecordService linkRecordService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SmtpMailUtil smtpMailUtil;
    @RequestMapping("/{url:[a-zA-Z0-9]{6}}")
    public String restoreUrl(@PathVariable("url") String url) throws UnsupportedEncodingException {
        Link link = new Link();
        link.setShort_url(url);
        link = linkService.getLinkInfoByShortUrl(link);
        if(link != null){
            Firewall firewall = new Firewall();
            firewall.setUser_id(link.getUser_id());
            firewall.setSoftware_id(link.getSoftware_id());
            firewall.setIp(util.getLocalIp(request));
            firewall.setObject(5);
            Firewall firewallCheck = firewallService.checkFirewallByFirewall(firewall);
            if(firewallCheck==null) {
                LinkRecord linkRecord = new LinkRecord();
                linkRecord.setLink_id(link.getId());
                String queryString=request.getQueryString();
                if(queryString==null){
                    queryString="";
                }else{
                    queryString="?"+queryString;
                }
                linkRecord.setOriginal_url(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI() +queryString);
                linkRecord.setLong_url(link.getLong_url());
                linkRecord.setShort_url(link.getShort_url());
                linkRecord.setState(1);
                linkRecord.setIp(util.getLocalIp(request));
                LinkRecord oldPosition = new LinkRecord();
                oldPosition= util.getIpAddressesByIpAsLinkRecord(linkRecord.getIp());
                linkRecord.setPosition(oldPosition.getPosition());
                linkRecord.setLatitude(oldPosition.getLatitude());
                linkRecord.setLongitude(oldPosition.getLongitude());
                linkRecord.setSystem(util.getClientSystem(request));
                linkRecord.setBrowser(util.getClientBrowser(request));
                linkRecord.setHeader(request.getHeader("user-agent"));
                linkRecordService.addLinkRecordByLinkRecord(linkRecord);
                return "redirect:"+link.getLong_url();
            }else{
                InterceptRecord interceptRecord = new InterceptRecord();
                interceptRecord.setUser_id(firewallCheck.getUser_id());
                interceptRecord.setSoftware_id(link.getSoftware_id());
                interceptRecord.setReal_ip(util.getLocalIp(request));
                interceptRecord.setFirewall_ip(firewallCheck.getIp());
                interceptRecord.setObject(5);
                interceptRecord.setSystem(util.getClientSystem(request));
                interceptRecord.setBrowser(util.getClientBrowser(request));
                interceptRecord.setHeader(request.getHeader("user-agent"));
                interceptRecordService.addInterceptRecordByInterceptRecord(interceptRecord);
                return "redirect:"+viewsHost+"403";
            }
        }else{
            return "redirect:"+viewsHost+"404";
        }
    }
}
