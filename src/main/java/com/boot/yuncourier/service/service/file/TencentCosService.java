package com.boot.yuncourier.service.service.file;

import com.boot.yuncourier.entity.service.file.TencentCos;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

public interface TencentCosService {
    int addTencentCosByTencentCos(TencentCos tencentCos);
    List<TencentCos> getTencentCosListByUser(User user);
    int deleteTencentCosByTencentCos(TencentCos tencentCos);
    TencentCos getTencentCosInfoByTencentCos(TencentCos tencentCos);
    int updateTencentCosByTencentCos(TencentCos tencentCos);
    int getTencentCosCount(TencentCos tencentCos);
    TencentCos getTencentCosInfoById(TencentCos tencentCos);
}
