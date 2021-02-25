package com.boot.yuncourier.dao.service.var;

import com.boot.yuncourier.entity.service.var.Var;
import com.boot.yuncourier.entity.service.var.VarRecord;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: VarRecordMapper
 * @Date: 2020-04-29
 */
@Mapper
public interface VarRecordMapper {
    int insertOne(VarRecord varRecord);
    List<VarRecord> getList(User user);
    int deleteOne(VarRecord varRecord);
    VarRecord getInfo(VarRecord varRecord);
}