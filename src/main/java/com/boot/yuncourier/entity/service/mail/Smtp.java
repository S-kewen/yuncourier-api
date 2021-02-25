package com.boot.yuncourier.entity.service.mail;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: Smtp-smtp實體類
 * @Date: 2020-02-18
 */
@Data
public class Smtp {
    private int id;
    private int user_id;
    private int software_id;
    private int state;
    private int type;
    private String host;
    private String account;
    private String password;
    private int port;
    private boolean ssl;
    private int priority;
    private String remark;
    private boolean deleted;
    private Date add_time;
    //以上字段來自數據庫
    private String software_name;
}
