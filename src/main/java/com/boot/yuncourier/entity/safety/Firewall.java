package com.boot.yuncourier.entity.safety;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: Firewall-防火墻實體類
 * @Date: 2020-02-01
 */
@Data
public class Firewall {
    private int id;
    private int user_id;
    private int software_id;
    private int state;
    private int type;
    private int object;
    private String ip;
    private String remark;
    private boolean deleted;
    private Date add_time;
    //以上來自sql
    private String software_name;
}
