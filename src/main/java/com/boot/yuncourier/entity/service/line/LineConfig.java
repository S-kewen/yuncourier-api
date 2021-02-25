package com.boot.yuncourier.entity.service.line;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @ClassName: LineConfig
 * @Description: 實體類
 * @Date: 2020-03-21
 */
@Data
public class LineConfig {
    private int id;
    private int user_id;
    private int state;
    private int type;
    private int software_id;
    private String line_config_name;
    private String channel_id;
    private String channel_secret;
    private String token;
    private String remark;
    private boolean deleted;
    private Date add_time;
    //以上來自sql
    private String software_name;
}
