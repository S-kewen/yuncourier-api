package com.boot.yuncourier.dao.safety;

import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: InterceptRecordMapper-攔截記錄dao
 * @Date: 2020-02-01
 */
@Mapper
public interface InterceptRecordMapper {
    int addInterceptRecordByInterceptRecord(InterceptRecord interceptRecord);
    List<InterceptRecord> getInterceptRecordListByUser(User user);
    int deleteInterceptRecordByInterceptRecord(InterceptRecord interceptRecord);
    InterceptRecord getInterceptRecordInfoByInterceptRecord(InterceptRecord interceptRecord);
}