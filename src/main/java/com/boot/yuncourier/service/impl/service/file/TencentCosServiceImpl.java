package com.boot.yuncourier.service.impl.service.file;

import com.boot.yuncourier.dao.service.file.TencentCosMapper;
import com.boot.yuncourier.entity.service.file.TencentCos;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.file.TencentCosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @ClassName: TencentCosServiceImlp
 * @Description: 騰訊雲cos配置service
 * @Date: 2020-02-23
 */

@Component
public class TencentCosServiceImpl implements TencentCosService {

    @Autowired
    private TencentCosMapper tencentCosMapper;
    @Override
    public int addTencentCosByTencentCos(TencentCos tencentCos) {
        return tencentCosMapper.addTencentCosByTencentCos(tencentCos);
    }
    @Override
    public List<TencentCos> getTencentCosListByUser(User user) {
        return tencentCosMapper.getTencentCosListByUser(user);
    }
    @Override
    public int deleteTencentCosByTencentCos(TencentCos tencentCos) {
        return tencentCosMapper.deleteTencentCosByTencentCos(tencentCos);
    }
    @Override
    public TencentCos getTencentCosInfoByTencentCos(TencentCos tencentCos) {
        return tencentCosMapper.getTencentCosInfoByTencentCos(tencentCos);
    }
    @Override
    public int updateTencentCosByTencentCos(TencentCos tencentCos) {
        return tencentCosMapper.updateTencentCosByTencentCos(tencentCos);
    }
    @Override
    public int getTencentCosCount(TencentCos tencentCos) {
        return tencentCosMapper.getTencentCosCount(tencentCos);
    }
    @Override
    public TencentCos getTencentCosInfoById(TencentCos tencentCos) {
        return tencentCosMapper.getTencentCosInfoById(tencentCos);
    }

}
