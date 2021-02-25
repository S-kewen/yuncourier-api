package com.boot.yuncourier.service.service.var;

import com.boot.yuncourier.entity.service.sms.Sms;
import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: VarService
 * @Date: 2020-04-29
 */

public interface VarService {
    int insertOne(Var var);
    List<Var> getList(User user);
    int deleteOne(Var var);
    Var getInfo(Var var);
    int updateOne(Var var);
    Var getById(Var var);
}
