package com.boot.yuncourier.util.impl;

import com.boot.yuncourier.util.Every8dSmsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

/**
 * @Author: skwen
 * @Description: Every8dSmsServiceImpl-every8d短信接口service
 * @Date: 2020-02-01
 */

@Component
public class Every8dSmsUtilImpl implements Every8dSmsUtil {
    /*
     * 1.subject(選填)簡訊主旨,，主旨不會隨著簡訊內容發送出去。⽤以註記本次發送之⽤途須使⽤ urlEncode
     * 2.msg(必填)簡訊發送內容,須使⽤ urlEncode
     * 3.phone(必填)接收手機號,多筆接收⼈時，逗點隔開( , )，須使⽤ urlEncode
     * 4.sendTime(選填)簡訊預定發送,立即發送請留空,時間格式為 yyyyMMddHHmnss
     * 5.retryTime(選填)簡訊有效期限,單位: 分鐘,若未指定,則以該簡訊平台之預設 1440 分鐘帶出。
     */
    private static String username="0963523380";
    private static String password="sdew";
    @Override
    public String sendSms(String subject,String msg,String phone,String sendTime,int retryTime) {
        URL url = null;
        HttpURLConnection connection = null;
        String result = null;
        try {
            url = new URL("https://oms.every8d.com/API21/HTTP/sendSMS.ashx?"
                    +"UID=" + username
                    +"&PWD=" + password
                    +"&SB=" +  URLEncoder.encode(subject, "UTF-8")
                    +"&MSG=" + URLEncoder.encode(msg, "UTF-8")
                    +"&DEST=" +URLEncoder.encode(phone, "UTF-8")
                    +"&ST=" +sendTime
                    +"&RETRYTIME=" + retryTime
            );
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(60000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(60000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod("GET");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
            out.flush();// 刷新
            out.close();// 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            result = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        if (result!= null) {
            return result;
        }
        return "";
    }
}
