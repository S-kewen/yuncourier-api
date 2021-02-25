package com.boot.yuncourier.service.console;

import com.boot.yuncourier.entity.home.Console;
import com.boot.yuncourier.entity.user.User;

/**
 * @Author: skwen
 * @Description: ConsoleService-控制台接口
 * @Date: 2020-02-01
 */

public interface ConsoleService {
    Console getConsoleInfoByUser(User user);
    Console getApiCountByUser(User user);
}
