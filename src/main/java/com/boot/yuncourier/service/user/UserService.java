package com.boot.yuncourier.service.user;

import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.entity.user.UserIndex;

/**
 * @Author: skwen
 * @Description: UserService-用戶相關接口
 * @Date: 2020-02-01
 */

public interface UserService {

    User getById(User User);
    User userLogin(User User);
    User getByUsername(User User);
    int updateUserInfoByUser(User user);
    int updatePasswordByUser(User user);
    UserIndex getUserIndexByUser(User user);
    int resetPasswordByUser(User user);
//    List<User> getAll();
}
