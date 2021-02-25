package com.boot.yuncourier.util.impl;

import com.boot.yuncourier.util.SmtpMailUtil;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: skwen
 * @Description: SmtpMailServiceImpl-smtp郵件操作service
 * @Date: 2020-02-01
 */

@Component
public class SmtpMailUtilImpl implements SmtpMailUtil {
    @Override
    public Map<String, String> sendEasyMail(String host, String account, String pwd, String to, String title, String msg) {
        Map<String,String> map = new HashMap<>();
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            Session ssn = Session.getInstance(props, new Authenticator() {
            });
            MimeMessage message = new MimeMessage(ssn);
            // 由邮件会话新建一个消息对象
            InternetAddress fromAddress = new InternetAddress(account);
            // 发件人的邮件地址
            message.setFrom(fromAddress);
            // 设置发件人
            InternetAddress toAddress = new InternetAddress(to);
            // 收件人的邮件地址
            message.addRecipient(Message.RecipientType.TO, toAddress);
            // 设置收件人
            message.setSubject(title);
            // 设置标题
            message.setText(msg);
            // 设置内容
            message.setSentDate(new Date());
            // 设置发信时间
//            message.addRecipients(Message.RecipientType.CC,cc);
            Transport transport = ssn.getTransport("smtp");
            transport.connect(host, account, pwd);
            //message.setContent(content, "text/html;charset=utf-8");
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            map.put("status","true");
            map.put("sender",account);
            return map;
        } catch (Exception e) {
            map.put("status","false");
            if(e.getMessage()==null){
                map.put("message","");
            }else{
                map.put("message",e.getMessage());
            }
            return map;
        }
    }
    @Override
    public Map<String, String> sendHtmlMail(String host, String account, String pwd, String to, String title, String msg) {
        Map<String,String> map = new HashMap<>();
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            Session ssn = Session.getInstance(props, new Authenticator() {
            });
            MimeMessage message = new MimeMessage(ssn);
            // 由邮件会话新建一个消息对象
            InternetAddress fromAddress = new InternetAddress(account);
            // 发件人的邮件地址
            message.setFrom(fromAddress);
            // 设置发件人
            InternetAddress toAddress = new InternetAddress(to);
            // 收件人的邮件地址
            message.addRecipient(Message.RecipientType.TO, toAddress);
            // 设置收件人
            message.setSubject(title);
            // 设置标题
            message.setText(msg);
            // 设置内容
            message.setSentDate(new Date());
            // 设置发信时间
//            message.addRecipients(Message.RecipientType.CC,cc);
            Transport transport = ssn.getTransport("smtp");
            transport.connect(host, account, pwd);
            message.setContent(msg, "text/html;charset=utf-8");
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            map.put("status","true");
            map.put("sender",account);
            return map;
        } catch (Exception e) {
            map.put("status","false");
            if(e.getMessage()==null){
                map.put("message","");
            }else{
                map.put("message",e.getMessage());
            }
            return map;
        }
    }
    @Override
    public Map<String, String> sendYunTechFlowWarnMail(String host, String account, String pwd, String to, String title, String ip,
                                 String Ext_Up, String Ext_Down, String Ins_Up, String Ins_Down, String flow, String ratio,
                                 String PoliceValue) {
        Map<String,String> map = new HashMap<>();
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            Session ssn = Session.getInstance(props, new Authenticator() {});
            MimeMessage message = new MimeMessage(ssn);
            // 由邮件会话新建一个消息对象
            InternetAddress fromAddress = new InternetAddress(account);
            // 发件人的邮件地址
            message.setFrom(fromAddress);
            // 设置发件人
            InternetAddress toAddress = new InternetAddress(to);
            // 收件人的邮件地址
            message.addRecipient(Message.RecipientType.TO, toAddress);
            // 设置收件人
            message.setSubject(title);
            // 设置标题
            String data = "<html>\r\n" + "<body>\r\n" + "<table border=\"1\" align=\"center\">\r\n"
                    + "<caption>您當前使用流量:" + flow + "Gb,已達到報警值" + PoliceValue + "Gb,請註意節流!</caption>" + "<tr>\r\n"
                    + "  <td>IP地址</td>\r\n" + "  <td>校外上傳/Gb</td>\r\n" + "  <td>校外下載/Gb</td>\r\n"
                    + "  <td>校内上傳/Gb</td>\r\n" + "  <td>校内下載/Gb</td>\r\n" + "  <td>傳輸比UL/DL</td>\r\n"
                    + "  <td>流量統計/Gb</td>\r\n" + "</tr>\r\n" + "<tr>\r\n" + "  <td>" + ip + "</td>\r\n" + "  <td>"
                    + Ext_Up + "</td>\r\n" + "  <td>" + Ext_Down + "</td>\r\n" + "  <td>" + Ins_Up + "</td>\r\n"
                    + "  <td>" + Ins_Down + "</td>\r\n" + "  <td>" + ratio + "</td>\r\n" + "  <td>" + flow + "</td>\r\n"
                    + "</tr>\r\n" + "</table>\r\n" + "</body>\r\n" + "</html>";
            message.setText(data);
            // 设置内容
            message.setSentDate(new Date());
            // 设置发信时间
            Transport transport = ssn.getTransport("smtp");
            transport.connect(host, account, pwd);
            message.setContent(data, "text/html;charset=utf-8");
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            map.put("status","true");
            map.put("sender",account);
            return map;
        } catch (Exception e) {
            map.put("status","false");
            if(e.getMessage()==null){
                map.put("message","");
            }else{
                map.put("message",e.getMessage());
            }
            return map;
        }
    }
}

