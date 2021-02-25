package com.boot.yuncourier.dao.service.line;

import com.boot.yuncourier.entity.service.file.TencentCos;
import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @ClassName: LineConfigMapper
 * @Description: dao
 * @Date: 2020-03-21
 */
@Mapper
public interface LineConfigMapper {
    int insertOne(LineConfig lineConfig);
    List<LineConfig> getList(User user);
    int deleteOne(LineConfig lineConfig);
    LineConfig getInfo(LineConfig lineConfig);
    int updateOne(LineConfig lineConfig);
    int getCount(LineConfig lineConfig);
    LineConfig getById(LineConfig lineConfig);
}