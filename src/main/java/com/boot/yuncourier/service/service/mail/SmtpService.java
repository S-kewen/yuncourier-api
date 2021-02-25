package com.boot.yuncourier.service.service.mail;

import com.boot.yuncourier.entity.service.mail.Smtp;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: SmtpService-smtp接口
 * @Date: 2020-02-18
 */

public interface SmtpService {
    int addSmtpBySmtp(Smtp smtp);
    List<Smtp> getSmtpListByUser(User user);
    int deleteSmtpBySmtp(Smtp smtp);
    Smtp getSmtpInfoBySmtp(Smtp smtp);
    int updateSmtpBySmtp(Smtp smtp);
    int getSmtpCount(Smtp smtp);
    List<Smtp> getSmtpListBySmtp(Smtp smtp);
}
