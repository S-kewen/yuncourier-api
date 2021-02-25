package com.boot.yuncourier.entity.service.var;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: VarRecord
 * @Date: 2020-04-29
 */
@Data
public class VarRecord {
    private int id;
    private int user_id;
    private int software_id;
    private int var_id;
    private int state;
    private int type;
    private String ip;
    private String request;
    private String header;
    private String remark;
    private boolean deleted;
    private Date add_time;
    //以上來自sql
    private String software_name;
    private String title;
}
