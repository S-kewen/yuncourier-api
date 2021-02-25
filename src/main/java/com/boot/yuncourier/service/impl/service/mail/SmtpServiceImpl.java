package com.boot.yuncourier.service.impl.service.mail;

import com.boot.yuncourier.dao.service.mail.SmtpMapper;
import com.boot.yuncourier.entity.service.mail.Smtp;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.mail.SmtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: SmtpServiceImpl-smtpservice
 * @Date: 2020-02-18
 */

@Component
public class SmtpServiceImpl implements SmtpService {

    @Autowired
    private SmtpMapper smtpMapper;
    @Override
    public int addSmtpBySmtp(Smtp smtp) {
        return smtpMapper.addSmtpBySmtp(smtp);
    }
    @Override
    public List<Smtp> getSmtpListByUser(User user) {
        return smtpMapper.getSmtpListByUser(user);
    }
    @Override
    public int deleteSmtpBySmtp(Smtp smtp) {
        return smtpMapper.deleteSmtpBySmtp(smtp);
    }
    @Override
    public Smtp getSmtpInfoBySmtp(Smtp smtp) {
        return smtpMapper.getSmtpInfoBySmtp(smtp);
    }
    @Override
    public int updateSmtpBySmtp(Smtp smtp) {
        return smtpMapper.updateSmtpBySmtp(smtp);
    }
    @Override
    public int getSmtpCount(Smtp smtp) {
        return smtpMapper.getSmtpCount(smtp);
    }
    @Override
    public List<Smtp> getSmtpListBySmtp(Smtp smtp) {
        return smtpMapper.getSmtpListBySmtp(smtp);
    }


}
