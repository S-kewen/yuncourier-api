package com.boot.yuncourier.service.safety;

import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: InterceptRecordService-攔截記錄接口
 * @Date: 2020-02-01
 */

public interface InterceptRecordService {

    int addInterceptRecordByInterceptRecord(InterceptRecord interceptRecord);
    List<InterceptRecord> getInterceptRecordListByUser(User user);
    int deleteInterceptRecordByInterceptRecord(InterceptRecord interceptRecord);
    InterceptRecord getInterceptRecordInfoByInterceptRecord(InterceptRecord interceptRecord);
}
