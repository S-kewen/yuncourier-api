package com.boot.yuncourier.dao.service.file;

import com.boot.yuncourier.entity.service.file.TencentCos;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @ClassName: TencentCosMapper
 * @Description: 騰訊雲cos配置dao
 * @Date: 2020-02-23
 */
@Mapper
public interface TencentCosMapper {
    int addTencentCosByTencentCos(TencentCos tencentCos);
    List<TencentCos> getTencentCosListByUser(User user);
    int deleteTencentCosByTencentCos(TencentCos tencentCos);
    TencentCos getTencentCosInfoByTencentCos(TencentCos tencentCos);
    int updateTencentCosByTencentCos(TencentCos tencentCos);
    int getTencentCosCount(TencentCos tencentCos);
    TencentCos getTencentCosInfoById(TencentCos tencentCos);
}