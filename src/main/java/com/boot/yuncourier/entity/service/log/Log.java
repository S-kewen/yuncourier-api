package com.boot.yuncourier.entity.service.log;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: Log
 * @Date: 2020-04-29
 */
@Data
public class Log {
    private int id;
    private int user_id;
    private int software_id;
    private int state;
    private int type;
    private String ip;
    private String mac;
    private String header;
    private String title;
    private String content;
    private String remark;
    private boolean deleted;
    private Date add_time;
    //以上來自sql
    private String software_name;
}
