package com.boot.yuncourier.service.impl.user;

import com.boot.yuncourier.dao.user.LoginRecordMapper;
import com.boot.yuncourier.entity.user.LoginRecord;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.user.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LoginRecordServiceImpl-登錄記錄service
 * @Date: 2020-02-01
 */


@Component
public class LoginRecordServiceImpl implements LoginRecordService {

    @Autowired
    private LoginRecordMapper loginRecordMapper;
    @Override
    public int addLoginRecordByLoginRecord(LoginRecord loginRecord) {
        return loginRecordMapper.addLoginRecordByLoginRecord(loginRecord);
    }
    @Override
    public LoginRecord getLoginRecordInfoByIp(LoginRecord loginRecord) {
        return loginRecordMapper.getLoginRecordInfoByIp(loginRecord);
    }
    @Override
    public List<LoginRecord> getLoginRecordListByUser(User user) {
        return loginRecordMapper.getLoginRecordListByUser(user);
    }
    @Override
    public int deleteLoginRecordByLoginRecord(LoginRecord loginRecord) {
        return loginRecordMapper.deleteLoginRecordByLoginRecord(loginRecord);
    }
    @Override
    public List<LoginRecord> getUserIndexLoginRecordListByUser(User user) {
        return loginRecordMapper.getUserIndexLoginRecordListByUser(user);
    }

}
