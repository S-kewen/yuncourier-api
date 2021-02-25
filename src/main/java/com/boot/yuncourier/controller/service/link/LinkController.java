package com.boot.yuncourier.controller.service.link;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.service.link.Link;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.service.link.LinkService;
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
 * @Description: LinkController-我的短連
 * @Date: 2020-01-31
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class LinkController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private Util util;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SmtpMailUtil smtpMailUtil;
    @LoginToken
    @RequestMapping("listMyLink")
    public Object listMyLink(int pageNumber,int pageSize,String content,int state,String startTime,String endTime) throws JSONException, ParseException {
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
            List<Link> select = linkService.getLinkListByUser(user);
            PageInfo<Link> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteLink")
    public Map<String, Object> deleteLink(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Link link= new Link();
            link.setUser_id(token.getId());
            link.setId(id);
            int count=linkService.deleteLinkByLink(link);
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
    @RequestMapping("resetShortUrl")
    public Map<String, Object> resetShortUrl(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Link link= new Link();
            link.setUser_id(token.getId());
            link.setId(id);
            link.setShort_url(util.getRandomStr(2,6,""));
            int count=linkService.updateLinkInfoByLink(link);
            if (count>0){
                map.put("status",0);
                map.put("tip","success");
            }else{
                map.put("status",-7);
                map.put("tip","重置失敗,請稍候再試");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
    @LoginToken
    @RequestMapping("changeLinkState")
    public Map<String, Object> changeLinkState(int id,int state) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Link link= new Link();
            link.setUser_id(token.getId());
            link.setId(id);
            link.setState(state);
            int count=linkService.updateLinkInfoByLink(link);
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
    @RequestMapping("addShort")
    public Map<String, Object> addShort(int softwareId,String longUrl, String remark) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Software software = new Software();
            software.setUser_id(token.getId());
            software.setId(softwareId);
            software=softwareService.getSoftwareInfoBySoftware(software);
            if (software==null){
                map.put("status",-10);
                map.put("tip","應用不存在");
                return map;
            }
            User user = new User();
            user.setId(software.getUser_id());
            user=userService.getById(user);
            Link link = new Link();
            longUrl= util.Base64ToStr(longUrl);
            link.setUser_id(user.getId());
            link.setLong_url(longUrl);
            link.setSystem(util.getClientSystem(request));
            link.setBrowser(util.getClientBrowser(request));
            link.setHeader(request.getHeader("user-agent"));
            if(linkService.getLinkInfoByLongUrl(link)!=null){
                map.put("status",-16);
                map.put("tip","該鏈接已存在");
                return map;
            }
            double cost=2;
            if (user.getBalance()<cost){
                map.put("status",-17);
                map.put("tip","用戶餘額不足,請充值後再試");
                map.put("balance",user.getBalance());
                return map;
            }
            link.setIp(util.getLocalIp(request));
            link.setSoftware_id(software.getId());
            link.setToken(software.getToken());
            link.setShort_url(util.getRandomStr(2,6,""));
            link.setRemark(remark);
            int count=linkService.addLinkByLink(link);
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
}
