package com.boot.yuncourier.entity.user;

import lombok.Data;

import java.security.Timestamp;
import java.util.Date;

/**
 * @Author: skwen
 * @Description: 實體類
 * @Date: 2020-02-01
 */

@Data
public class User {
   private int id;
   private String username;
   private String password;
   private int state;
   private String phone;
   private String email;
   private float balance;
   private String remark;
   private Date add_time;
   //以上來自sql
   private String content;
   private int type;
   private Date startTime;
   private Date endTime;
}