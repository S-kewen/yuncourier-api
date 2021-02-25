package com.boot.yuncourier.entity.service.mail;

import lombok.Data;

import java.security.Timestamp;
import java.util.Date;

/**
 * @Author: skwen
 * @Description: Mail-我的郵件實體類
 * @Date: 2020-02-01
 */
@Data
public class Mail {
    private int id;
    private int user_id;
    private int state;
    private int software_id;
    private String token;
    private String ip;
    private String receive_mail;
    private String title;
    private String msg;
    private int content_type;
    private String sender;
    private Date send_time;
    private String system;
    private String browser;
    private String header;
    private String remark;
    private String api_request;
    private String api_respond;
    private boolean deleted;
    private Date add_time;
    //以上來自sql
    private String software_name;
}
