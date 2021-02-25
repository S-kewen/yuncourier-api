package com.boot.yuncourier.entity.service.sms;

import lombok.Data;

import java.security.Timestamp;
import java.util.Date;

/**
 * @Author: skwen
 * @Description: Sms-我的短信實體類
 * @Date: 2020-02-01
 */
@Data
public class Sms {
    private int id;
    private int user_id;
    private int type;
    private int state;
    private int software_id;
    private String token;
    private String ip;
    private String subject;
    private String receive_phone;
    private String msg;
    private Date send_time;
    private float cost;
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
