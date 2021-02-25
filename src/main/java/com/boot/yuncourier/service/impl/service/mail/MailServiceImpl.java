package com.boot.yuncourier.service.impl.service.mail;

import com.boot.yuncourier.dao.service.mail.MailMapper;
import com.boot.yuncourier.entity.service.mail.Mail;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: MailServiceImpl-我的郵件service
 * @Date: 2020-02-01
 */

@Component
public class MailServiceImpl implements MailService {

    @Autowired
    private MailMapper mailMapper;
    @Override
    public int addMailByMail(Mail Mail) {
        return mailMapper.addMailByMail(Mail);
    }
    @Override
    public List<Mail> getMailListByUser(User user) {
        return mailMapper.getMailListByUser(user);
    }
    @Override
    public int deleteMailByMail(Mail mail) {
        return mailMapper.deleteMailByMail(mail);
    }
    @Override
    public Mail getMailInfoByMail(Mail mail) {
        return mailMapper.getMailInfoByMail(mail);
    }
    @Override
    public int updateMailInfoByMail(Mail mail) {
        return mailMapper.updateMailInfoByMail(mail);
    }


}
