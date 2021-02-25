package com.boot.yuncourier.entity.system;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @Description: Performance-系統性能負載實體類
 * @Date: 2020-02-01
 */
@Data
public class Performance {
    private int id;
    private float cpu;
    private float ram;
    private boolean deleted;
    private Date add_time;
}
