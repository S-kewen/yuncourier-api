package com.boot.yuncourier.entity.home;

import lombok.Data;

/**
 * @Author: skwen
 * @Description: Console-控制台實體類
 * @Date: 2020-02-01
 */
@Data
public class Console {
    private int todayMailNum;
    private int allMailNum;
    private int todaySmsNum;
    private int allSmsNum;
    private int todayLinkNum;
    private int allLinkNum;
    private int todayLineNum;
    private int allLineNum;
    private int todayInterceptNum;
    private int allInterceptNum;
    private float todayPay;
    private float allPay;
    private int mailFailNum;
    private int smsFailNum;
    private int linkFailNum;
    private int lineFailNum;
    private float cpu;
    private float ram;
}
