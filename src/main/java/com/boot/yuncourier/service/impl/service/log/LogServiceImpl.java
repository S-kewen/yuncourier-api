package com.boot.yuncourier.service.impl.service.log;

import com.boot.yuncourier.dao.service.log.LogMapper;
import com.boot.yuncourier.dao.service.var.VarMapper;
import com.boot.yuncourier.entity.service.log.Log;
import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.log.LogService;
import com.boot.yuncourier.service.service.var.VarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LogServiceImpl
 * @Date: 2020-04-29
 */

@Component
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;
    @Override
    public int insertOne(Log log) {
        return logMapper.insertOne(log);
    }
    @Override
    public List<Log> getList(User user) {
        return logMapper.getList(user);
    }
    @Override
    public int deleteOne(Log log) {
        return logMapper.deleteOne(log);
    }
    @Override
    public Log getInfo(Log log) {
        return logMapper.getInfo(log);
    }
    @Override
    public int updateOne(Log log) {
        return logMapper.updateOne(log);
    }
    @Override
    public Log getById(Log log) {
        return logMapper.getById(log);
    }

}
