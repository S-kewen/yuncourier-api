package com.boot.yuncourier.entity.user;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: News-系統消息實體類
 * @Date: 2020-02-01
 */
@Data
public class News {
    private int id;
    private int user_id;
    private int state;
    private String sender;
    private String title;
    private String msg;
    private boolean deleted;
    private Date add_time;
}
