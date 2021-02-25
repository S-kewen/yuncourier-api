package com.boot.yuncourier.dao.service.sms;

import com.boot.yuncourier.entity.service.sms.Sms;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: SmsMapper-我的短信dao
 * @Date: 2020-02-01
 */
@Mapper
public interface SmsMapper {
    int addSmsBySms(Sms Sms);
    List<Sms> getSmsListByUser(User user);
    int deleteSmsBySms(Sms sms);
    Sms getSmsInfoBySms(Sms sms);
    int updateSmsInfoBySms(Sms sms);
}