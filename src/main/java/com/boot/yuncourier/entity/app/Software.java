package com.boot.yuncourier.entity.app;



import lombok.Data;

import java.security.Timestamp;
import java.util.Date;

/**
 * @Author: skwen
 * @Description: Software-我的應用實體類
 * @Date: 2020-02-01
 */
@Data
public class Software {
    private int id;
    private int user_id;
    private String software_name;
    private int state;
    private String token;
    private String remark;
    private boolean deleted;
    private Date add_time;
}
