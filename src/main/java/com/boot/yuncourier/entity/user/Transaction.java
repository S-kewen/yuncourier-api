package com.boot.yuncourier.entity.user;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: Transaction-交易記錄實體類
 * @Date: 2020-02-01
 */

@Data
public class Transaction {
    private int id;
    private int user_id;
    private int state;
    private int type;
    private String title;
    private float amount;
    private float commission;
    private float actual_amount;
    private float balance;
    private boolean deleted;
    private Date add_time;
}
