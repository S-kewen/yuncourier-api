package com.boot.yuncourier.dao.service.line;

import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.service.line.PushMessage;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @ClassName: PushMessageMapper
 * @Description: dao
 * @Date: 2020-03-21
 */
@Mapper
public interface PushMessageMapper {
    int insertOne(PushMessage pushMessage);
    List<PushMessage> getList(User user);
    int deleteOne(PushMessage linepushMessageConfig);
    PushMessage getInfo(PushMessage pushMessage);
}