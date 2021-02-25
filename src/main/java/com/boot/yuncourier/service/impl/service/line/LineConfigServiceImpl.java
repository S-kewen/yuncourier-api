package com.boot.yuncourier.service.impl.service.line;

import com.boot.yuncourier.dao.service.file.TencentCosMapper;
import com.boot.yuncourier.dao.service.line.LineConfigMapper;
import com.boot.yuncourier.entity.service.file.TencentCos;
import com.boot.yuncourier.entity.service.line.LineConfig;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.file.TencentCosService;
import com.boot.yuncourier.service.service.line.LineConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @ClassName: LineConfigServiceImpl
 * @Description: service
 * @Date: 2020-03-21
 */

@Component
public class LineConfigServiceImpl implements LineConfigService {

    @Autowired
    private LineConfigMapper lineConfigMapper;
    @Override
    public int insertOne(LineConfig lineConfig) {
        return lineConfigMapper.insertOne(lineConfig);
    }
    @Override
    public List<LineConfig> getList(User user) {
        return lineConfigMapper.getList(user);
    }
    @Override
    public int deleteOne(LineConfig lineConfig) {
        return lineConfigMapper.deleteOne(lineConfig);
    }
    @Override
    public LineConfig getInfo(LineConfig lineConfig) {
        return lineConfigMapper.getInfo(lineConfig);
    }
    @Override
    public int updateOne(LineConfig lineConfig) {
        return lineConfigMapper.updateOne(lineConfig);
    }
    @Override
    public int getCount(LineConfig lineConfig) {
        return lineConfigMapper.getCount(lineConfig);
    }
    @Override
    public LineConfig getById(LineConfig lineConfig) {
        return lineConfigMapper.getById(lineConfig);
    }

}
