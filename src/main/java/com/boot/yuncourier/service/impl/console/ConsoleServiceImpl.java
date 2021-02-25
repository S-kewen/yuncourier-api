package com.boot.yuncourier.service.impl.console;

import com.boot.yuncourier.dao.home.ConsoleMapper;
import com.boot.yuncourier.entity.home.Console;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.console.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: skwen
 * @Description: ConsoleServiceImpl-控制台service
 * @Date: 2020-02-01
 */

@Component
public class ConsoleServiceImpl implements ConsoleService {
    @Autowired
    private ConsoleMapper consoleMapper;
    @Override
    public Console getConsoleInfoByUser(User user) {
        return consoleMapper.getConsoleInfoByUser(user);
    }
    @Override
    public Console getApiCountByUser(User user) {
        return consoleMapper.getApiCountByUser(user);
    }
}
