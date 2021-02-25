package com.boot.yuncourier.entity.service.link;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: Link-我的短連實體類
 * @Date: 2020-02-01
 */
@Data
public class Link {
    private int id;
    private int user_id;
    private int state;
    private int software_id;
    private String token;
    private String ip;
    private String long_url;
    private String short_url;
    private String system;
    private String browser;
    private String header;
    private String remark;
    private boolean deleted;
    private Date add_time;
    //以上來自sql
    private String software_name;
}
