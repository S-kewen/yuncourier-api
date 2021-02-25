package com.boot.yuncourier.dao.home;

import com.boot.yuncourier.entity.home.Console;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: skwen
 * @Description: ConsoleMapper-控制台dao
 * @Date: 2020-01-31
 */
@Mapper
public interface ConsoleMapper {
    Console getConsoleInfoByUser(User user);
    Console getApiCountByUser(User user);
}