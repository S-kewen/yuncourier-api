package com.boot.yuncourier.service.impl.service.var;

import com.boot.yuncourier.dao.service.sms.SmsMapper;
import com.boot.yuncourier.dao.service.var.VarMapper;
import com.boot.yuncourier.entity.service.sms.Sms;
import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.sms.SmsService;
import com.boot.yuncourier.service.service.var.VarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: VarServiceImpl
 * @Date: 2020-04-29
 */

@Component
public class VarServiceImpl implements VarService {

    @Autowired
    private VarMapper varMapper;
    @Override
    public int insertOne(Var var) {
        return varMapper.insertOne(var);
    }
    @Override
    public List<Var> getList(User user) {
        return varMapper.getList(user);
    }
    @Override
    public int deleteOne(Var var) {
        return varMapper.deleteOne(var);
    }
    @Override
    public Var getInfo(Var var) {
        return varMapper.getInfo(var);
    }
    @Override
    public int updateOne(Var var) {
        return varMapper.updateOne(var);
    }
    @Override
    public Var getById(Var var) {
        return varMapper.getById(var);
    }

}
