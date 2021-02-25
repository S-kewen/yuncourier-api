package com.boot.yuncourier.dao.service.mail;

import com.boot.yuncourier.entity.service.mail.Mail;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: MailMapper-我的郵件dao
 * @Date: 2020-02-01
 */
@Mapper
public interface MailMapper {
    int addMailByMail(Mail Mail);
    List<Mail> getMailListByUser(User user);
    int deleteMailByMail(Mail mail);
    Mail getMailInfoByMail(Mail mail);
    int updateMailInfoByMail(Mail mail);
}