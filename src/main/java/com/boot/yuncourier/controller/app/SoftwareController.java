package com.boot.yuncourier.controller.app;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.util.TokenUtil;
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
 * @Description: SoftwareController-我的應用
 * @Date: 2020-01-31
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class SoftwareController {

    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TokenUtil tokenUtil;

    @LoginToken
    @RequestMapping("listMySoftware")
    public Object listMySoftware(int pageNumber,int pageSize,String content,int state,String startTime,String endTime) throws JSONException, ParseException {
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
            List<Software> select = softwareService.getSoftwareListByUser(user);
            PageInfo<Software> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteSoftware")
    public Map<String, Object> deleteSoftware(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Software software = new Software();
            software.setUser_id(token.getId());
            software.setId(id);
            int count=softwareService.deleteSoftwareBySoftware(software);
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
    @RequestMapping("addSoftware")
    public Map<String, Object> addSoftware(String softwareName,String remark) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Software software = new Software();
            software.setUser_id(token.getId());
            software.setSoftware_name(softwareName);
            if (softwareService.checkSoftwareNameBySoftware(software)==0){
                software.setRemark(remark);
                int count=softwareService.addSoftware(software);
                if (count>0){
                    map.put("status",0);
                    map.put("tip","success");
                }else{
                    map.put("status",-7);
                    map.put("tip","添加失敗,請稍候再試");
                }
            }else{
                map.put("status",-9);
                map.put("tip","該應用已存在,請修改名稱後再試");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
    @LoginToken
    @RequestMapping("changeSoftwareState")
    public Map<String, Object> changeSoftwareState(int id,int state) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Software software = new Software();
            software.setUser_id(token.getId());
            software.setId(id);
            software.setState(state);
            if(state==1 || state==2){
                int count=softwareService.updateSoftwareInfoBySoftware(software);
                if (count>0){
                    map.put("status",0);
                    map.put("tip","success");
                }else{
                    map.put("status",-7);
                    map.put("tip","修改失敗,請稍候再試");
                }
            }else{
                map.put("status", -1);
                map.put("tip", "弟弟,跟我玩這套?");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
    @LoginToken
    @RequestMapping("getSoftwareInfo")
    public Map<String, Object> getSoftwareInfo(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            Software software = new Software();
            software.setUser_id(token.getId());
            software.setId(id);
            software=softwareService.getSoftwareInfoBySoftware(software);
            if (software!=null){
                map.put("status",0);
                map.put("tip","success");
                map.put("softwareName",software.getSoftware_name());
                map.put("token",software.getToken());
                map.put("remark",software.getRemark());
            }else{
                map.put("status",-10);
                map.put("tip","該應用不存在");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
    @LoginToken
    @RequestMapping("updateSoftware")
    public Map<String, Object> updateSoftware(int id,String softwareName,String token,String remark) {
        Map<String,Object> map = new HashMap<>();
        Token token2 = new Token();
        token2= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token2!=null) {
            Software software = new Software();
            software.setUser_id(token2.getId());
            software.setId(id);
            software.setToken(token);
            software.setSoftware_name(softwareName);
            software.setRemark(remark);
            if (softwareService.checkSoftwareNameBySoftware(software)==0){
                if(token.length()==32){
                    int count=softwareService.updateSoftwareInfoBySoftware(software);
                    if (count>0){
                        map.put("status",0);
                        map.put("tip","success");
                    }else{
                        map.put("status",-10);
                        map.put("tip","修改失敗,請稍候再試");
                    }
                }else{
                    map.put("status", -1);
                    map.put("tip", "token必須由32位的字母和數字組成");
                }
            }else{
                map.put("status", -1);
                map.put("tip", "該應用名稱已存在,請修改後再試");
            }

        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

}
