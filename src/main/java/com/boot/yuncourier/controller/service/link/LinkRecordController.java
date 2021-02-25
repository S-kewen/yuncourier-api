package com.boot.yuncourier.controller.service.link;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.service.link.Link;
import com.boot.yuncourier.entity.service.link.LinkRecord;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.service.link.LinkRecordService;
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
 * @Description: LinkRecordController-短連轉發記錄
 * @Date: 2020-01-31
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class LinkRecordController {
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
    private LinkRecordService linkRecordService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SmtpMailUtil smtpMailUtil;
    @LoginToken
    @RequestMapping("listMyLinkRecord")
    public Object listMyLinkRecord(int pageNumber,int pageSize,String content,int state,String startTime,String endTime) throws JSONException, ParseException {
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
            List<LinkRecord> select = linkRecordService.getLinkRecordListByUser(user);
            PageInfo<LinkRecord> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteLinkRecord")
    public Map<String, Object> deleteLinkRecord(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            LinkRecord linkRecord= new LinkRecord();
            linkRecord.setId(id);
            linkRecord=linkRecordService.getById(linkRecord);
            if(linkRecord!=null){
                Link link = new Link();
                link.setUser_id(token.getId());
                link.setId(linkRecord.getLink_id());
                link=linkService.getById(link);
                if(link!=null){
                    int count=linkRecordService.deleteLinkRecordByLinkRecord(linkRecord);
                    if (count>0){
                        map.put("status",0);
                        map.put("tip","success");
                    }else{
                        map.put("status",-7);
                        map.put("tip","刪除失敗,請稍候再試");
                    }
                }else{
                    map.put("status", -3);
                    map.put("tip", "該記錄不存在");
                }
            }else{
                map.put("status", -2);
                map.put("tip", "該記錄不存在");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
}
