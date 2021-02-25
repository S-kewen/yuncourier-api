package com.boot.yuncourier.service.impl.service.var;

import com.boot.yuncourier.dao.service.var.VarMapper;
import com.boot.yuncourier.dao.service.var.VarRecordMapper;
import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.service.var.VarRecord;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.var.VarRecordService;
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
public class VarRecordServiceImpl implements VarRecordService {

    @Autowired
    private VarRecordMapper varRecordMapper;
    @Override
    public int insertOne(VarRecord varRecord) {
        return varRecordMapper.insertOne(varRecord);
    }
    @Override
    public List<VarRecord> getList(User user) {
        return varRecordMapper.getList(user);
    }
    @Override
    public int deleteOne(VarRecord varRecord) {
        return varRecordMapper.deleteOne(varRecord);
    }
    @Override
    public VarRecord getInfo(VarRecord varRecord) {
        return varRecordMapper.getInfo(varRecord);
    }

}
