package com.boot.yuncourier.util.impl;

import com.boot.yuncourier.entity.service.line.LineNews;
import com.boot.yuncourier.util.LineUtil;
import com.boot.yuncourier.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author: skwen
 * @ClassName: LineUtilImpl
 * @Description: 工具類
 * @Date: 2020-03-21
 */
@Component
public class LineUtilImpl implements LineUtil {
    @Value("${line.messagingApi.url}")
    private String messagingApiUrl;
    @Autowired
    private Util util;

    @Override
    public String pushMessage(String to, String type, String text, String accessToken) {
        Map<String, Object> map = new HashMap<>();
        LineNews lineNews = new LineNews();
        List<LineNews> body = new LinkedList<>();
        lineNews.setType(type);
        lineNews.setText(text);
        body.add(lineNews);
        map.put("to", to);
        map.put("messages", body);
        return post(messagingApiUrl, accessToken, map);
    }

    public String post(String url, String accessToken, Map<String, Object> data) {
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Authorization", "Bearer {" + accessToken + "}");
                httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(data),
                        ContentType.create("text/json", "UTF-8")));
                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                return result;
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
