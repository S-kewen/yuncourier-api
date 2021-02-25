package com.boot.yuncourier.entity.service.link;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: LinkRecord-短連轉發記錄實體類
 * @Date: 2020-02-01
 */
@Data
public class LinkRecord {
    private int id;
    private int link_id;
    private String original_url;
    private String long_url;
    private String short_url;
    private int state;
    private String ip;
    private String position;
    private double latitude;
    private double longitude;
    private String system;
    private String browser;
    private String header;
    private boolean deleted;
    private Date add_time;
    //以上來自sql
    private String software_name;
}
