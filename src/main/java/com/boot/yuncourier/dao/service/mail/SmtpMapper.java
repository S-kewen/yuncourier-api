package com.boot.yuncourier.dao.service.mail;

import com.boot.yuncourier.entity.service.mail.Smtp;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: SmtpMapper-smtpdao
 * @Date: 2020-02-18
 */
@Mapper
public interface SmtpMapper {
    int addSmtpBySmtp(Smtp smtp);
    List<Smtp> getSmtpListByUser(User user);
    int deleteSmtpBySmtp(Smtp smtp);
    Smtp getSmtpInfoBySmtp(Smtp smtp);
    int updateSmtpBySmtp(Smtp smtp);
    int getSmtpCount(Smtp smtp);
    List<Smtp> getSmtpListBySmtp(Smtp smtp);
}