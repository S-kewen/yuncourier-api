package com.boot.yuncourier.dao.service.log;

import com.boot.yuncourier.entity.service.log.Log;
import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LogMapper
 * @Date: 2020-04-29
 */
@Mapper
public interface LogMapper {
    int insertOne(Log log);
    List<Log> getList(User user);
    int deleteOne(Log log);
    Log getInfo(Log log);
    int updateOne(Log log);
    Log getById(Log log);
}