package com.boot.yuncourier.dao.user;

import com.boot.yuncourier.entity.user.User;

import com.boot.yuncourier.entity.user.UserIndex;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: skwen
 * @Description: UserMapper-用戶相關dao
 * @Date: 2020-02-01
 */
@Mapper
public interface UserMapper {
    User getById(User user);
    User userLogin(User user);
    User getByUsername(User user);
    int updateUserInfoByUser(User user);
    int updatePasswordByUser(User user);
    UserIndex getUserIndexByUser(User user);
    int resetPasswordByUser(User user);
}