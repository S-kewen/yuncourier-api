package com.boot.yuncourier.util.impl;

import com.boot.yuncourier.entity.service.link.LinkRecord;
import com.boot.yuncourier.entity.user.LoginRecord;
import com.boot.yuncourier.service.service.link.LinkRecordService;
import com.boot.yuncourier.service.user.LoginRecordService;
import com.boot.yuncourier.util.*;
import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/**
 * @Author: skwen
 * @Description: OtherServiceImpl-其他類service
 * @Date: 2020-02-01
 */

@Component
public class UtilImpl implements Util {
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private LinkRecordService linkRecordService;
    public static final String KEY_ALGORITHM="AES";
    public static final String CIPHER_ALGORITHM="AES/ECB/PKCS7Padding";
    @Override
    public  String getLocalIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || ip.indexOf(":") > -1) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                ip = "";
            }
        }
        if (ip==null){
            ip="";
        }
        return ip;
    }
    @Override
    public Date getNowTimeDate() throws ParseException {
        String result;
        Calendar time = Calendar.getInstance();// 可以对每个时间域单独修改
        time.add(Calendar.MONTH, 1);
        String year = String.valueOf(time.get(Calendar.YEAR));
        String month = String.valueOf(time.get(Calendar.MONTH));
        String date = String.valueOf(time.get(Calendar.DATE));
        String hour = String.valueOf(time.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(time.get(Calendar.MINUTE));
        String second = String.valueOf(time.get(Calendar.SECOND));
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (date.length() == 1) {
            date = "0" + date;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        result = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(result);

    }
    @Override
    public String getIpAddressesByIp(String ip) throws UnsupportedEncodingException {
        // 这里调用http://ip-api.com
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setIp(ip);
        loginRecord = loginRecordService.getLoginRecordInfoByIp(loginRecord);
        if (loginRecord!=null && loginRecord.getPosition()!=null) {
            return loginRecord.getPosition();
        }
        URL url = null;
        HttpURLConnection connection = null;
        String result = null;
        try {
            url = new URL("http://ip-api.com/json/" + ip);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(10000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(10000);// 设置读取数据超时时间，单位毫秒
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
            String theString = buffer.toString();
            result=getJsonStr(theString,"country")+ "|"
                    +getJsonStr(theString,"city")+ "|"
                    +getJsonStr(theString,"as")+ "|<"
                    +getJsonFloat(theString,"lat")+ ","
                    +getJsonFloat(theString,"lon")+">";
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
        return "0";
    }
    @Override
    public LinkRecord getIpAddressesByIpAsLinkRecord(String ip) throws UnsupportedEncodingException {
        // 这里调用http://ip-api.com
        LinkRecord linkRecord = new LinkRecord();
        LinkRecord result = new LinkRecord();
        linkRecord.setIp(ip);
        linkRecord = linkRecordService.getLinkRecordInfoByIp(linkRecord);
        if (linkRecord!=null) {
            return linkRecord;
        }
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL("http://ip-api.com/json/" + ip);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(10000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(10000);// 设置读取数据超时时间，单位毫秒
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
            String theString = buffer.toString();
            result.setPosition(getJsonStr(theString,"country")+ "|"
                    +getJsonStr(theString,"city")+ "|"
                    +getJsonStr(theString,"as"));
            result.setLatitude(getJsonFloat(theString,"lat"));
            result.setLongitude(getJsonFloat(theString,"lon"));
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
        return result;
    }
    public  int getJsonInt(String json,String key) {
        JSONObject object = JSONObject.parseObject(json);
        return object.getIntValue(key);
    }
    public  float getJsonFloat(String json,String key) {
        JSONObject object = JSONObject.parseObject(json);
        return object.getFloatValue(key);
    }
    public  Boolean getJsonBoolean(String json,String key) {
        JSONObject object = JSONObject.parseObject(json);
        return object.getBooleanValue(key);
    }
    public  String getJsonStr(String json,String key) {
        JSONObject object = JSONObject.parseObject(json);
        return object.getString(key);
    }
    public  Date getJsonDate(String json,String key) {
        JSONObject object = JSONObject.parseObject(json);
        return object.getDate(key);
    }
    @Override
    public String getUrlValue(String url, String key) {
        if (url.substring(url.length()-1,url.length()).equals("&")){
            url=url.substring(0,url.length()-1);
        }
        String params = url.substring(url.indexOf("?") + 1, url.length());
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        return split.get(key);
    }
    @Override
    public String getRandomStr(int type,int length,String exChars){

        if(length<=0) return "";

        StringBuffer code=new StringBuffer();
        int i=0;
        Random r=new Random();

        switch(type)
        {

            //仅数字
            case 0:
                while(i<length){
                    int t=r.nextInt(10);
                    if(exChars==null||exChars.indexOf(t+"")<0){//排除特殊字符
                        code.append(t);
                        i++;
                    }
                }
                break;

            //仅字母（即大写字母、小写字母混合）
            case 1:
                while(i<length){
                    int t=r.nextInt(123);
                    if((t>=97||(t>=65&&t<=90))&&(exChars==null||exChars.indexOf((char)t)<0)){
                        code.append((char)t);
                        i++;
                    }
                }
                break;

            //数字、大写字母、小写字母混合
            case 2:
                while(i<length){
                    int t=r.nextInt(123);
                    if((t>=97||(t>=65&&t<=90)||(t>=48&&t<=57))&&(exChars==null||exChars.indexOf((char)t)<0)){
                        code.append((char)t);
                        i++;
                    }
                }
                break;

            //数字、大写字母混合
            case 3:
                while(i<length){
                    int t=r.nextInt(91);
                    if((t>=65||(t>=48&&t<=57))&&(exChars==null||exChars.indexOf((char)t)<0)){
                        code.append((char)t);
                        i++;
                    }
                }
                break;

            //数字、小写字母混合
            case 4:
                while(i<length){
                    int t=r.nextInt(123);
                    if((t>=97||(t>=48&&t<=57))&&(exChars==null||exChars.indexOf((char)t)<0)){
                        code.append((char)t);
                        i++;
                    }
                }
                break;

            //仅大写字母
            case 5:
                while(i<length){
                    int t=r.nextInt(91);
                    if((t>=65)&&(exChars==null||exChars.indexOf((char)t)<0)){
                        code.append((char)t);
                        i++;
                    }
                }
                break;

            //仅小写字母
            case 6:
                while(i<length){
                    int t=r.nextInt(123);
                    if((t>=97)&&(exChars==null||exChars.indexOf((char)t)<0)){
                        code.append((char)t);
                        i++;
                    }
                }
                break;

        }

        return code.toString();
    }
    @Override
    public String getMd5(String str) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = str.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str2[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str2[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str2[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String StrToBase64(String str) {
        if(null != str){
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(str.getBytes());
        }
        return null;
    }
    @Override
    public String Base64ToStr(String str) {
        if (null != str) {
            Base64.Decoder decoder = Base64.getDecoder();
            try {
                return new String(decoder.decode(str.getBytes()), "GBK");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }
    @Override
    public String getClientBrowser(HttpServletRequest request){
        String browserVersion = null;
        String header = request.getHeader("user-agent");
        if(header == null || header.equals("")){
            return "unknown";
        }
        if (header.indexOf("MSIE") > 0) {
            browserVersion = "IE";
        } else if (header.indexOf("QQBrowser") > 0) {
            browserVersion = "QQBrowser";
        } else if (header.indexOf("Firefox") > 0) {
            browserVersion = "Firefox";
        } else if (header.indexOf("Chrome") > 0) {
            browserVersion = "Chrome";
        } else if (header.indexOf("Safari") > 0) {
            browserVersion = "Safari";
        } else if (header.indexOf("QQBrowser") > 0) {
            browserVersion = "QQBrowser";
        } else if (header.indexOf("Camino") > 0) {
            browserVersion = "Camino";
        } else if (header.indexOf("Konqueror") > 0) {
            browserVersion = "Konqueror";
        } else {
            browserVersion = "unknown";
        }
        return browserVersion;
    }
    @Override
    public String getClientSystem(HttpServletRequest request){
        String systenInfo = null;
        String header = request.getHeader("user-agent");
        if(header == null || header.equals("")){
            return "unknown";
        }
        //得到用户的操作系统
        if (header.indexOf("NT 6.0") > 0) {
            systenInfo = "Windows Vista/Server 2008";
        }else if (header.indexOf("iPhone") > 0) {
            systenInfo = "iPhone";
        }else if (header.indexOf("Android") > 0) {
            systenInfo = "Android";
        } else if (header.indexOf("NT 5.2") > 0) {
            systenInfo = "Windows Server 2003";
        } else if (header.indexOf("NT 5.1") > 0) {
            systenInfo = "Windows XP";
        } else if (header.indexOf("NT 6.0") > 0) {
            systenInfo = "Windows Vista";
        } else if (header.indexOf("NT 6.1") > 0) {
            systenInfo = "Windows 7";
        } else if (header.indexOf("NT 6.2") > 0) {
            systenInfo = "Windows Slate";
        } else if (header.indexOf("NT 6.3") > 0) {
            systenInfo = "Windows 9";
        } else if (header.indexOf("NT 10.0") > 0) {
            systenInfo = "Windows 10";
        } else if (header.indexOf("NT 5") > 0) {
            systenInfo = "Windows 2000";
        } else if (header.indexOf("NT 4") > 0) {
            systenInfo = "Windows NT4";
        } else if (header.indexOf("Me") > 0) {
            systenInfo = "Windows Me";
        } else if (header.indexOf("98") > 0) {
            systenInfo = "Windows 98";
        } else if (header.indexOf("95") > 0) {
            systenInfo = "Windows 95";
        } else if (header.indexOf("Mac") > 0) {
            systenInfo = "Mac";
        } else if (header.indexOf("Unix") > 0) {
            systenInfo = "UNIX";
        } else if (header.indexOf("Linux") > 0) {
            systenInfo = "Linux";
        } else if (header.indexOf("SunOS") > 0) {
            systenInfo = "SunOS";
        } else {
            systenInfo = "unknown";
        }
        return systenInfo;
    }

    @Override
    public String aesEncrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            kgen.init(128, new SecureRandom(password.getBytes()));// 利用用户密码作为随机数初始化出
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return parseByte2HexStr(result);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content
     *            AES加密过过的内容
     * @param password
     *            加密时的密码
     * @return 明文
     */
    @Override
    public String aesDecrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(parseHexStr2Byte(content));
            return parseByte2HexStr(result); // 明文
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    @Override
    public byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
    }
