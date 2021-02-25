package com.boot.yuncourier.util;

import java.util.Map;

/**
 * @Author: skwen
 * @Description: SmtpMailService-smtp郵件接口
 * @Date: 2020-02-01
 */

public interface SmtpMailUtil {
    Map<String, String> sendEasyMail(String host, String account, String pwd, String to, String title, String msg);
    Map<String, String> sendHtmlMail(String host, String account, String pwd, String to, String title, String msg);
    Map<String, String> sendYunTechFlowWarnMail(String host, String account, String pwd, String to, String title, String ip,
                                                String Ext_Up, String Ext_Down, String Ins_Up, String Ins_Down, String flow, String ratio,
                                                String PoliceValue);
}
