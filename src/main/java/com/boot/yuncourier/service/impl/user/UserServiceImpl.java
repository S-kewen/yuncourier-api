package com.boot.yuncourier.service.impl.user;

import com.boot.yuncourier.dao.user.UserMapper;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.entity.user.UserIndex;
import com.boot.yuncourier.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: skwen
 * @Description: UserServiceImpl-用戶相關service
 * @Date: 2020-02-01
 */

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User getById(User User) {
        return userMapper.getById(User);
    }
    @Override
    public User userLogin(User User) {
        return userMapper.userLogin(User);
    }
    @Override
    public User getByUsername(User User) {
        return userMapper.getByUsername(User);
    }
    @Override
    public int updateUserInfoByUser(User user){
        return userMapper.updateUserInfoByUser(user);
    }
    @Override
    public int updatePasswordByUser(User user){
        return userMapper.updatePasswordByUser(user);
    }
    @Override
    public UserIndex getUserIndexByUser(User user){
        return userMapper.getUserIndexByUser(user);
    }
    @Override
    public int resetPasswordByUser(User user){
        return userMapper.resetPasswordByUser(user);
    }


//    @Override
//    public List<User> getAll() {
//        return userMapper.selectAll();
//    }
}
