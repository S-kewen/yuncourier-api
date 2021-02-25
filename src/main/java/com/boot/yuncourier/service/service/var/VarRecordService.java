package com.boot.yuncourier.service.service.var;

import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.service.var.VarRecord;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: VarRecordService
 * @Date: 2020-04-29
 */

public interface VarRecordService {
    int insertOne(VarRecord varRecord);
    List<VarRecord> getList(User user);
    int deleteOne(VarRecord varRecord);
    VarRecord getInfo(VarRecord varRecord);
}
