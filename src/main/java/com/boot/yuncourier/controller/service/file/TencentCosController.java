package com.boot.yuncourier.controller.service.file;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.service.file.TencentCos;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.service.file.TencentCosService;
import com.boot.yuncourier.service.user.UserService;
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
 * @ClassName: TencentCosController
 * @Description: 騰訊雲cos配置Conteoller
 * @Date: 2020-02-23
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class TencentCosController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private Util util;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private TencentCosService tencentCosService;
    @Autowired
    private HttpServletRequest request;
    @LoginToken
    @RequestMapping("listMyCos")
    public Object listMyCos(int pageNumber,int pageSize,String content,int state,String startTime,String endTime) throws JSONException, ParseException {
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
            List<TencentCos> select = tencentCosService.getTencentCosListByUser(user);
            PageInfo<TencentCos> pageInfo = new PageInfo<>(select);
            return pageInfo;
        }else{
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }
    @LoginToken
    @RequestMapping("deleteCos")
    public Map<String, Object> deleteCos(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            TencentCos tencentCos= new TencentCos();
            tencentCos.setUser_id(token.getId());
            tencentCos.setId(id);
            int count=tencentCosService.deleteTencentCosByTencentCos(tencentCos);
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
    @RequestMapping("changeCosState")
    public Map<String, Object> changeCosState(int id,int state) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            TencentCos tencentCos= new TencentCos();
            tencentCos.setUser_id(token.getId());
            tencentCos.setId(id);
            tencentCos.setState(state);
            int count=tencentCosService.updateTencentCosByTencentCos(tencentCos);
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
    @RequestMapping("getCosInfo")
    public Map<String, Object> getCosInfo(int id) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            TencentCos tencentCos= new TencentCos();
            tencentCos.setUser_id(token.getId());
            tencentCos.setId(id);
            tencentCos=tencentCosService.getTencentCosInfoByTencentCos(tencentCos);
            if (tencentCos!=null && !tencentCos.isDeleted()){
                map.put("status",0);
                map.put("tip","success");
                map.put("cosName",tencentCos.getCos_name());
                map.put("defaultPath",tencentCos.getDefault_path());
                map.put("softwareId",tencentCos.getSoftware_id());
                map.put("bucketName",tencentCos.getBucket_name());
                map.put("region",tencentCos.getRegion());
                map.put("secretId",tencentCos.getSecret_id());
                map.put("secretKey", util.StrToBase64(tencentCos.getSecret_key()));
                map.put("remark",tencentCos.getRemark());
            }else{
                map.put("status",-7);
                map.put("tip","該配置不存在");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
    @LoginToken
    @RequestMapping("addCos")
    public Map<String, Object> addCos(int softwareId, String cosName, String defaultPath, String bucketName, int region, String secretId, String secretKey, String remark) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            if(softwareId!=-1){
                Software software = new Software();
                software.setUser_id(token.getId());
                software.setId(softwareId);
                software=softwareService.getSoftwareInfoBySoftware(software);
                if (software==null){
                    map.put("status",-10);
                    map.put("tip","應用不存在");
                    return map;
                }
            }
            User user = new User();
            user.setId(token.getId());
            user=userService.getById(user);
            TencentCos tencentCos = new TencentCos();
            tencentCos.setCos_name(cosName);
            tencentCos.setUser_id(token.getId());
            if(tencentCosService.getTencentCosCount(tencentCos)>0){
                map.put("status",-29);
                map.put("tip","該配置已存在");
                return map;
            }
            tencentCos.setDefault_path(defaultPath);
            tencentCos.setSoftware_id(softwareId);
            tencentCos.setBucket_name(bucketName);
            tencentCos.setRegion(region);
            tencentCos.setSecret_id(secretId);
            tencentCos.setSecret_key(secretKey);
            tencentCos.setRemark(remark);
            int count=tencentCosService.addTencentCosByTencentCos(tencentCos);
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
    @LoginToken
    @RequestMapping("updateCos")
    public Map<String, Object> updateCos(int id,int softwareId,String cosName, String defaultPath,String bucketName, int region, String secretId, String secretKey, String remark) {
        Map<String,Object> map = new HashMap<>();
        Token token = new Token();
        token= tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token!=null) {
            TencentCos tencentCos= new TencentCos();
            tencentCos.setCos_name(cosName);
            tencentCos.setUser_id(token.getId());
            if(tencentCosService.getTencentCosCount(tencentCos)>1){
                map.put("status",-29);
                map.put("tip","該配置已存在");
                return map;
            }
            tencentCos.setDefault_path(defaultPath);
            tencentCos.setSoftware_id(softwareId);
            tencentCos.setBucket_name(bucketName);
            tencentCos.setRegion(region);
            tencentCos.setSecret_id(secretId);
            tencentCos.setSecret_key(secretKey);
            tencentCos.setRemark(remark);
            tencentCos.setId(id);
            int count=tencentCosService.updateTencentCosByTencentCos(tencentCos);
            if (count>0){
                map.put("status",0);
                map.put("tip","success");
            }else{
                map.put("status",-25);
                map.put("tip","修改失敗,請稍後再試");
            }
        }else{
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }
}
