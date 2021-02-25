package com.boot.yuncourier.service.impl.service.line;

import com.boot.yuncourier.dao.service.line.LineConfigMapper;
import com.boot.yuncourier.dao.service.line.PushMessageMapper;
import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.service.line.PushMessage;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.line.LineConfigService;
import com.boot.yuncourier.service.service.line.PushMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @ClassName: PushMessageServiceImpl
 * @Description: service
 * @Date: 2020-03-21
 */

@Component
public class PushMessageServiceImpl implements PushMessageService {

    @Autowired
    private PushMessageMapper pushMessageMapper;
    @Override
    public int insertOne(PushMessage pushMessage) {
        return pushMessageMapper.insertOne(pushMessage);
    }
    @Override
    public List<PushMessage> getList(User user) {
        return pushMessageMapper.getList(user);
    }
    @Override
    public int deleteOne(PushMessage pushMessage) {
        return pushMessageMapper.deleteOne(pushMessage);
    }
    @Override
    public PushMessage getInfo(PushMessage pushMessage) {
        return pushMessageMapper.getInfo(pushMessage);
    }
}
