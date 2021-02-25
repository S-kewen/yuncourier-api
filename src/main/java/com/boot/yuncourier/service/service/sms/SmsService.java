package com.boot.yuncourier.service.service.sms;

import com.boot.yuncourier.entity.service.sms.Sms;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: SmsService我的短信接口
 * @Date: 2020-02-01
 */

public interface SmsService {

    int addSmsBySms(Sms sms);
    List<Sms> getSmsListByUser(User user);
    int deleteSmsBySms(Sms sms);
    Sms getSmsInfoBySms(Sms sms);
    int updateSmsInfoBySms(Sms sms);
}
