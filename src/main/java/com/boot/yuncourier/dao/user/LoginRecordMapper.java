package com.boot.yuncourier.dao.user;

import com.boot.yuncourier.entity.user.LoginRecord;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LoginRecordMapper-登錄記錄dao
 * @Date: 2020-02-01
 */
@Mapper
public interface LoginRecordMapper {
    int addLoginRecordByLoginRecord(LoginRecord loginRecord);
    LoginRecord getLoginRecordInfoByIp(LoginRecord loginRecord);
    List<LoginRecord> getLoginRecordListByUser(User user);
    int deleteLoginRecordByLoginRecord(LoginRecord loginRecord);
    List<LoginRecord> getUserIndexLoginRecordListByUser(User user);
}