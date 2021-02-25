package com.boot.yuncourier.service.impl.safety;

import com.boot.yuncourier.dao.safety.InterceptRecordMapper;
import com.boot.yuncourier.entity.safety.InterceptRecord;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.safety.InterceptRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: InterceptRecordServiceImpl-攔截記錄service
 * @Date: 2020-02-01
 */

@Component
public class InterceptRecordServiceImpl implements InterceptRecordService {

    @Autowired
    private InterceptRecordMapper interceptRecordMapper;
    @Override
    public int addInterceptRecordByInterceptRecord(InterceptRecord interceptRecord) {
        return interceptRecordMapper.addInterceptRecordByInterceptRecord(interceptRecord);
    }
    @Override
    public List<InterceptRecord> getInterceptRecordListByUser(User user) {
        return interceptRecordMapper.getInterceptRecordListByUser(user);
    }
    @Override
    public int deleteInterceptRecordByInterceptRecord(InterceptRecord interceptRecord) {
        return interceptRecordMapper.deleteInterceptRecordByInterceptRecord(interceptRecord);
    }
    @Override
    public InterceptRecord getInterceptRecordInfoByInterceptRecord(InterceptRecord interceptRecord) {
        return interceptRecordMapper.getInterceptRecordInfoByInterceptRecord(interceptRecord);
    }

}
