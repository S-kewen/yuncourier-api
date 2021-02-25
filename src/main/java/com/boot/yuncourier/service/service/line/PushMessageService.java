package com.boot.yuncourier.service.service.line;

import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.service.line.PushMessage;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

public interface PushMessageService {
    int insertOne(PushMessage pushMessage);
    List<PushMessage> getList(User user);
    int deleteOne(PushMessage pushMessage);
    PushMessage getInfo(PushMessage pushMessage);
}
