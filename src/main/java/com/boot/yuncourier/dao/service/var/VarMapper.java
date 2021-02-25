package com.boot.yuncourier.dao.service.var;

import com.boot.yuncourier.entity.service.sms.Sms;
import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: VarMapper
 * @Date: 2020-04-29
 */
@Mapper
public interface VarMapper {
    int insertOne(Var var);
    List<Var> getList(User user);
    int deleteOne(Var var);
    Var getInfo(Var var);
    int updateOne(Var var);
    Var getById(Var var);
}