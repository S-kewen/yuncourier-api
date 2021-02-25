package com.boot.yuncourier.entity.user;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: UserIndex-個人中心實體類
 * @Date: 2020-02-01
 */

@Data
public class UserIndex {
    private int todayMailNum;
    private int allMailNum;
    private int todaySmsNum;
    private int allSmsNum;
    private int todayLinkNum;
    private int allLinkNum;
    private int mailFailNum;
    private int smsFailNum;
    private int linkFailNum;
    private float todayPay;
    private float allPay;
    private int user_id;
    private String username;
    private String password;
    private int state;
    private String phone;
    private String email;
    private float balance;
    private String remark;
    private Date add_time;
}
