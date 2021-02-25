package com.boot.yuncourier.entity.safety;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: InterceptRecord-攔截記錄實體類
 * @Date: 2020-02-01
 */
@Data
public class InterceptRecord {
    private int id;
    private int user_id;
    private int software_id;
    private String real_ip;
    private String firewall_ip;
    private int object;
    private String system;
    private String browser;
    private String header;
    private boolean deleted;
    private Date add_time;
//    以上來自SQL
    private String software_name;
    private int whiteState;
}
