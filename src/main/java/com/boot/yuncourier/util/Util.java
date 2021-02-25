package com.boot.yuncourier.util;

import com.boot.yuncourier.entity.service.link.LinkRecord;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;

/**
 * @Author: skwen
 * @Description: OtherService-其他類接口
 * @Date: 2020-02-01
 */

public interface Util {
     String getLocalIp(HttpServletRequest request);
     Date getNowTimeDate() throws ParseException;
     String getIpAddressesByIp(String ip) throws UnsupportedEncodingException;
     String getUrlValue(String url, String key);
     String getRandomStr(int type,int length,String exChars);
     String getMd5(String str);
     String StrToBase64(String str);
     String Base64ToStr(String str);
     String getClientBrowser(HttpServletRequest request);
     String getClientSystem(HttpServletRequest request);
     LinkRecord getIpAddressesByIpAsLinkRecord(String ip) throws UnsupportedEncodingException;
     String aesEncrypt(String content, String password);
     String aesDecrypt(String content, String password);
     String parseByte2HexStr(byte buf[]);
     byte[] parseHexStr2Byte(String hexStr);
}
