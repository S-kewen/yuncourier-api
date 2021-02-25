package com.boot.yuncourier.util.impl;

import com.boot.yuncourier.util.TencentCloudCosUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: skwen
 * @ClassName: TencentCloudCosUtilImpl
 * @Description: 騰訊雲cos工具實現類
 * @Date: 2020-02-29
 */

@Component
public class TencentCloudCosUtilImpl implements TencentCloudCosUtil {

    @Override
    public Map<String, Object> getObjectUrl(String bucketName, String region, String key, String secretId, String secretKey) {
        Map<String, Object> map = new HashMap<>();
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region2 = new Region(region);
        ClientConfig clientConfig = new ClientConfig(region2);
        COSClient cosClient = new COSClient(cred, clientConfig);
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        COSObject cosObject = cosClient.getObject(getObjectRequest);
        COSObjectInputStream cosObjectInput = cosObject.getObjectContent();

        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        Date expirationDate = new Date(System.currentTimeMillis() + 60L * 1000L);
        req.setExpiration(expirationDate);
        URL url = cosClient.generatePresignedUrl(req);
        if(url!=null && url.toString()!=""){
            map.put("status", 0);
            map.put("tip", "success");
            map.put("url", url.toString());
        }else{
            map.put("status", -1);
            map.put("tip", "create failed");
        }
        return map;
    }
}
