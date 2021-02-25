package com.boot.yuncourier.entity.user;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: LoginRecord-登錄記錄實體類
 * @Date: 2020-02-01
 */
@Data
public class LoginRecord {
    private int id;
    private int user_id;
    private int state;
    private int type;
    private String ip;
    private String position;
    private String system;
    private String browser;
    private String header;
    private boolean deleted;
    private Date add_time;
}
