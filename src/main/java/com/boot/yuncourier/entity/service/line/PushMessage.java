package com.boot.yuncourier.entity.service.line;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @ClassName: PushMessage
 * @Description: 實體類
 * @Date: 2020-03-21
 */
@Data
public class PushMessage {
    private int id;
    private int user_id;
    private int state;
    private int software_id;
    private String token;
    private int line_config_id;
    private String to;
    private String type;
    private String text;
    private String ip;
    private String system;
    private String brower;
    private String header;
    private String remark;
    private String api_request;
    private String api_response;
    private boolean deleted;
    private Date add_time;
    //以上來自sql
    private String software_name;
    private String line_config_name;
}
