package com.boot.yuncourier.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: Token-token實體類
 * @Date: 2020-02-01
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private int id;
    private String username;
    private String password;
    private String role;
    private String ip;
    private Date add_time;
    private Date expire_time;
}
