package com.boot.yuncourier.service.impl.service.sms;

import com.boot.yuncourier.dao.service.sms.SmsMapper;
import com.boot.yuncourier.entity.service.sms.Sms;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: SmsServiceImpl-我的短信service
 * @Date: 2020-02-01
 */

@Component
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsMapper smsMapper;
    @Override
    public int addSmsBySms(Sms sms) {
        return smsMapper.addSmsBySms(sms);
    }
    @Override
    public List<Sms> getSmsListByUser(User user) {
        return smsMapper.getSmsListByUser(user);
    }
    @Override
    public int deleteSmsBySms(Sms sms) {
        return smsMapper.deleteSmsBySms(sms);
    }
    @Override
    public Sms getSmsInfoBySms(Sms sms) {
        return smsMapper.getSmsInfoBySms(sms);
    }
    @Override
    public int updateSmsInfoBySms(Sms sms) {
        return smsMapper.updateSmsInfoBySms(sms);
    }



}
