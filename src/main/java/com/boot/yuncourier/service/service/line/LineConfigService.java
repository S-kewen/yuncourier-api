package com.boot.yuncourier.service.service.line;

import com.boot.yuncourier.entity.service.file.TencentCos;
import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

public interface LineConfigService {
    int insertOne(LineConfig lineConfig);
    List<LineConfig> getList(User user);
    int deleteOne(LineConfig lineConfig);
    LineConfig getInfo(LineConfig lineConfig);
    int updateOne(LineConfig lineConfig);
    int getCount(LineConfig lineConfig);
    LineConfig getById(LineConfig lineConfig);
}
