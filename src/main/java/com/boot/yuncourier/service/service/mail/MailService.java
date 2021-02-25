package com.boot.yuncourier.service.service.mail;

import com.boot.yuncourier.entity.service.mail.Mail;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: MailService-我的郵件接口
 * @Date: 2020-02-01
 */

public interface MailService {

    int addMailByMail(Mail Mail);
    List<Mail> getMailListByUser(User user);
    int deleteMailByMail(Mail mail);
    Mail getMailInfoByMail(Mail mail);
    int updateMailInfoByMail(Mail mail);
}
