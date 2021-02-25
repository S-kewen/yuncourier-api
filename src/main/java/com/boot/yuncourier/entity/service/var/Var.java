package com.boot.yuncourier.entity.service.var;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: Var
 * @Date: 2020-04-29
 */
@Data
public class Var {
    private int id;
    private int user_id;
    private int software_id;
    private int state;
    private int type;
    private String token;
    private Date expire_time;
    private String title;
    private String content;
    private String remark;
    private boolean deleted;
    private Date add_time;
    //以上來自sql
    private String software_name;
}
