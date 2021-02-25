package com.boot.yuncourier.controller.service.file;

import com.boot.yuncourier.annotation.LoginToken;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.service.file.CosFile;
import com.boot.yuncourier.entity.service.file.TencentCos;
import com.boot.yuncourier.entity.service.link.Link;
import com.boot.yuncourier.entity.system.Token;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import com.boot.yuncourier.service.service.file.CosFileService;
import com.boot.yuncourier.service.service.file.TencentCosService;
import com.boot.yuncourier.service.service.link.LinkService;
import com.boot.yuncourier.service.user.LoginRecordService;
import com.boot.yuncourier.service.user.NewsService;
import com.boot.yuncourier.service.user.UserService;
import com.boot.yuncourier.util.TencentCloudCaptchaUtil;
import com.boot.yuncourier.util.TokenUtil;
import com.boot.yuncourier.util.Util;
import com.boot.yuncourier.util.TencentCloudCosUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: skwen
 * @ClassName: CosFileController
 * @Description: 對象儲存文件controller
 * @Date: 2020-02-25
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class CosFileController {
    @Value("${link.addShort.cost}")
    private double linkAddShortCost;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private TencentCloudCaptchaUtil tencentcloudapiService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private Util util;
    @Autowired
    private CosFileService cosFileService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LinkService linkService;
    @Autowired
    private SoftwareService softwareService;
    @Autowired
    private TencentCosService tencentCosService;
    @Autowired
    private TencentCloudCosUtil tencentCloudCosUtil;

    @LoginToken
    @RequestMapping("listMyFile")
    public Object listMyFile(int pageNumber, int pageSize, String content, int state, int fileType, String startTime, String endTime) throws JSONException, ParseException {
        JSONObject json = new JSONObject();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            user.setId(token.getId());
            user.setContent(content);
            user.setState(state);
            user.setType(fileType);
            if (startTime != null && startTime != "") {
                user.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if (endTime != null && endTime != "") {
                user.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }
            PageHelper.startPage(pageNumber, pageSize);
            List<CosFile> select = cosFileService.getCosFileListByUser(user);
            PageInfo<CosFile> pageInfo = new PageInfo<>(select);
            return pageInfo;
        } else {
            json.put("status", -1);
            json.put("tip", "登錄已失效，請重新登錄");
        }
        return json;
    }

    @LoginToken
    @RequestMapping("deleteFile")
    public Map<String, Object> deleteFile(int id) {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            CosFile cosFile = new CosFile();
            cosFile.setUser_id(token.getId());
            cosFile.setId(id);
            int count = cosFileService.deleteCosFileByCosFile(cosFile);
            if (count > 0) {
                map.put("status", 0);
                map.put("tip", "success");
            } else {
                map.put("status", -7);
                map.put("tip", "刪除失敗,請稍候再試");
            }

        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @LoginToken
    @RequestMapping("getFileInfo")
    public Map<String, Object> getFileInfo(int id) {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            CosFile cosFile = new CosFile();
            cosFile.setUser_id(token.getId());
            cosFile.setId(id);
            cosFile = cosFileService.getCosFileInfoByCosFile(cosFile);
            if (cosFile != null) {
                map.put("status", 0);
                map.put("tip", "success");
                map.put("cosId", cosFile.getCos_id());
                map.put("fileName", cosFile.getFile_name());
                map.put("password", cosFile.getPassword());
                map.put("md5", cosFile.getMd5());
                map.put("size", cosFile.getSize());
                map.put("state", cosFile.getState());
                map.put("openType", cosFile.getOpen_type());
                map.put("bucket", cosFile.getBucket());
                map.put("filePath", cosFile.getFile_path());
                map.put("key", cosFile.getKey());
                map.put("region", cosFile.getRegion());
                map.put("requestId", cosFile.getRequest_id());
                map.put("depositType", cosFile.getDeposit_type());
                map.put("expireTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cosFile.getExpire_time()));
                map.put("sign", cosFile.getSign());
                map.put("downloadUrl", cosFile.getDownload_url());
                map.put("cosUrl", cosFile.getCos_url());
                map.put("shortUrl", cosFile.getShort_url());
                map.put("addTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cosFile.getAdd_time()));
                map.put("remark", cosFile.getRemark());
            } else {
                map.put("status", -11);
                map.put("tip", "該文件不存在,請稍候再試");
            }

        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @LoginToken
    @RequestMapping("changeFileState")
    public Map<String, Object> changeFileState(int id, int state, int openType, int depositType) {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            CosFile cosFile = new CosFile();
            cosFile.setUser_id(token.getId());
            cosFile.setId(id);
            cosFile.setState(state);
            cosFile.setOpen_type(openType);
            cosFile.setDeposit_type(depositType);
            int count = cosFileService.updateCosFileByCosFile(cosFile);
            if (count > 0) {
                map.put("status", 0);
                map.put("tip", "success");
            } else {
                map.put("status", -7);
                map.put("tip", "修改失敗,請稍候再試");
            }

        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @LoginToken
    @RequestMapping("seeFile")
    public Map<String, Object> seeFile(int id) {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            CosFile cosFile = new CosFile();
            cosFile.setUser_id(token.getId());
            cosFile.setId(id);
            cosFile = cosFileService.getCosFileInfoByCosFile(cosFile);
            if (cosFile != null) {
                map.put("status", 0);
                map.put("tip", "success");
                map.put("url", cosFile.getCos_url());
            } else {
                map.put("status", -7);
                map.put("tip", "該文件不存在,請稍後再試");
            }

        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @LoginToken
    @RequestMapping("createShortUrlByFile")
    public Map<String, Object> createShortUrlByFile(int id) {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            CosFile cosFile = new CosFile();
            cosFile.setUser_id(token.getId());
            cosFile.setId(id);
            cosFile = cosFileService.getCosFileInfoByCosFile(cosFile);
            if (cosFile != null) {
                User user = new User();
                user.setId(token.getId());
                user = userService.getById(user);
                if (user == null) {
                    map.put("status", -30);
                    map.put("tip", "請求被拒絕,用戶不存在");
                    return map;
                } else if (user.getBalance() < linkAddShortCost) {
                    map.put("status", -5);
                    map.put("tip", "餘額不足,請充值後再試");
                    return map;
                }
                TencentCos tencentCos = new TencentCos();
                tencentCos.setId(cosFile.getCos_id());
                tencentCos.setUser_id(cosFile.getUser_id());
                tencentCos = tencentCosService.getTencentCosInfoByTencentCos(tencentCos);
                if (tencentCos == null) {
                    map.put("status", -31);
                    map.put("tip", "請求被拒絕,Cos配置不存在");
                    return map;
                }
                Software software = new Software();
                software.setUser_id(cosFile.getUser_id());
                software.setId(tencentCos.getSoftware_id());
                software = softwareService.getSoftwareInfoBySoftware(software);
                if (software == null) {
                    map.put("status", -32);
                    map.put("tip", "請求被拒絕,應用不存在");
                    return map;
                }
                Link link = new Link();
                link.setUser_id(cosFile.getUser_id());
                link.setSoftware_id(software.getId());
                link.setToken(software.getToken());
                link.setIp(util.getLocalIp(request));
                link.setLong_url(cosFile.getDownload_url() + "?id=" + cosFile.getId() + "&sign=" + cosFile.getSign());
                link.setShort_url(util.getRandomStr(2, 6, ""));
                link.setSystem(util.getClientSystem(request));
                link.setBrowser(util.getClientBrowser(request));
                link.setHeader(request.getHeader("user-agent"));
                link.setRemark("對象儲存文件短連");
                int count = linkService.addLinkByLink(link);
                if (count > 0) {
                    CosFile update = new CosFile();
                    update.setUser_id(token.getId());
                    update.setId(cosFile.getId());
                    update.setShort_url(link.getShort_url());
                    count = cosFileService.updateCosFileByCosFile(update);
                    if (count > 0) {
                        map.put("status", 0);
                        map.put("tip", "success");
                    } else {
                        map.put("status", -33);
                        map.put("tip", "新建失敗,請稍後再試");
                    }
                } else {
                    map.put("status", -33);
                    map.put("tip", "生成失敗,請稍後再試");
                }
            } else {
                map.put("status", -7);
                map.put("tip", "該文件不存在");
            }

        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @LoginToken
    @RequestMapping("updateFile")
    public Map<String, Object> updateFile(int id, String fileName, String password, int state, int openType, int depositType, String expireTime, String remark) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            CosFile cosFile = new CosFile();
            cosFile.setUser_id(token.getId());
            cosFile.setId(id);
            cosFile.setFile_name(fileName);
            cosFile.setPassword(password);
            cosFile.setState(state);
            cosFile.setOpen_type(openType);
            cosFile.setDeposit_type(depositType);
            cosFile.setExpire_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expireTime));
            cosFile.setRemark(remark);
            int count = cosFileService.updateCosFileByCosFile(cosFile);
            if (count > 0) {
                map.put("status", 0);
                map.put("tip", "success");
            } else {
                map.put("status", -7);
                map.put("tip", "修改失敗,請稍候再試");
            }
        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }

    @LoginToken
    @RequestMapping("addFile")
    public Map<String, Object> addFile(int cosId, String fileName, String md5, long size, String bucket, String filePath, String key, String region, String requestId, String remark, String cosUrl, int fileType) throws ParseException, UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            CosFile cosFile = new CosFile();
            cosFile.setUser_id(token.getId());
            cosFile.setBucket(bucket);
            cosFile.setFile_path(filePath);
            cosFile.setKey(key);
            cosFile.setRegion(region);
            CosFile oldCosFile = cosFileService.getCosFileInfoByKey(cosFile);
            if (oldCosFile != null) {
                oldCosFile.setCos_id(cosId);
                oldCosFile.setFile_name(fileName);
                oldCosFile.setMd5(md5);
                oldCosFile.setSize(size);
                oldCosFile.setRequest_id(requestId);
                oldCosFile.setFile_type(fileType);
                oldCosFile.setAdd_time(util.getNowTimeDate());
                int count = cosFileService.updateCosFileByCosFile(oldCosFile);
                if (count > 0) {
                    map.put("status", 0);
                    map.put("tip", "success");
                    map.put("mode", 2);
                    return map;
                }
            }
            cosFile.setCos_id(cosId);
            cosFile.setFile_name(fileName);
            cosFile.setMd5(md5);
            cosFile.setSize(size);
            cosFile.setRequest_id(requestId);
            cosFile.setFile_type(fileType);
            cosFile.setRemark(remark);
            if(key.indexOf("/")==-1){
                cosFile.setCos_url(cosUrl+ "/"+URLEncoder.encode( key, "UTF-8" ));
            }else{
                cosFile.setCos_url(cosUrl+ "/"+URLEncoder.encode( key, "UTF-8" ).replace("%2F","/"));
            }
            cosFile.setDownload_url("https://courier.iskwen.com/downloadCosFile");
            cosFile.setShort_url("");
            int count = cosFileService.addCosFileByCosFile(cosFile);
            if (count > 0) {
                map.put("status", 0);
                map.put("tip", "success");
                map.put("mode", 1);
            } else {
                map.put("status", -7);
                map.put("tip", "上傳後台數據失敗,請稍後再試");
            }

        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }

        return map;
    }
    @RequestMapping("downloadFile")
    public Map<String, Object> downloadFile(int id, String sign, String password) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        if (sign == null || sign == "" || id == 0) {
            map.put("status", -32);
            map.put("tip", "參數缺失");
            return map;
        }
        CosFile cosFile = new CosFile();
        cosFile.setId(id);
        cosFile.setSign(sign);
        cosFile = cosFileService.getCosFileInfoBySign(cosFile);
        if (cosFile != null) {
            if(cosFile.getOpen_type()!=2 || cosFile.getPassword().equals(password)){
            if(cosFile.getState()==1){
                    if(cosFile.getDeposit_type()!=2 || cosFile.getExpire_time().after(new Date())){
                        TencentCos tencentCos = new TencentCos();
                        tencentCos.setId(cosFile.getCos_id());
                        tencentCos=tencentCosService.getTencentCosInfoById(tencentCos);
                        if(tencentCos!=null){
                            Map<String, Object> result=tencentCloudCosUtil.getObjectUrl(cosFile.getBucket(),cosFile.getRegion(),cosFile.getKey(),tencentCos.getSecret_id(),tencentCos.getSecret_key());
                            if(result.get("status").equals(0)){
                                map.put("status", 0);
                                map.put("tip", "success");
                                map.put("url", result.get("url"));
                            }else{
                                map.put("status", -36);
                                map.put("tip", "url生成失敗,請檢查cos配置");
                            }
                        }else{
                            map.put("status", -37);
                            map.put("tip", "查詢cos配置失敗");
                        }
                    }else{
                        map.put("status", -35);
                        map.put("tip", "該文件已過期");
                    }
                }else{
                    map.put("status", -39);
                    map.put("tip", "該文件目前不可讀");
                }
            }else{
                map.put("status", -38);
                map.put("tip", "密匙錯誤");
            }
        } else {
            map.put("status", -33);
            map.put("tip", "該文件不存在");
        }

        return map;
    }
    @LoginToken
    @RequestMapping("clearFile")
    public Map<String, Object> clearFile(int cosId, String password) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Token token = new Token();
        token = tokenUtil.parseToken(request.getHeader("Authorization"));
        if (token != null) {
            User user = new User();
            user.setUsername(token.getUsername());
            user.setPassword(password);
            user=userService.userLogin(user);
            if(user!=null){
                CosFile cosFile = new CosFile();
                cosFile.setUser_id(token.getId());
                cosFile.setCos_id(cosId);
                if(cosFileService.getCosFileCountByCosFile(cosFile)>0){
                    int count = cosFileService.clearCosFileByCosFile(cosFile);
                    if (count > 0) {
                        map.put("status", 0);
                        map.put("tip", "success");
                        map.put("count", count);
                    } else {
                        map.put("status", -7);
                        map.put("tip", "修改失敗,請稍候再試");
                    }
                }else{
                    map.put("status", -41);
                    map.put("tip", "該Cos配置下空空如也,無需清空");
                }
            }else{
                map.put("status", -40);
                map.put("tip", "密碼錯誤");
            }
        } else {
            map.put("status", -1);
            map.put("tip", "登錄已失效，請重新登錄");
        }
        return map;
    }


}
