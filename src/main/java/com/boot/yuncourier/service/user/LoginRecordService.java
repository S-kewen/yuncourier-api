package com.boot.yuncourier.service.user;


import com.boot.yuncourier.entity.user.LoginRecord;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LoginRecordService-登錄記錄接口
 * @Date: 2020-02-01
 */

public interface LoginRecordService {
    int addLoginRecordByLoginRecord(LoginRecord loginRecord);
    LoginRecord getLoginRecordInfoByIp(LoginRecord loginRecord);
    List<LoginRecord> getLoginRecordListByUser(User user);
    int deleteLoginRecordByLoginRecord(LoginRecord loginRecord);
    List<LoginRecord> getUserIndexLoginRecordListByUser(User user);
}
