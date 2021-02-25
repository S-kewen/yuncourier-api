package com.boot.yuncourier.dao.system;


import com.boot.yuncourier.entity.system.Performance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: PerformanceMapper-系統性能負載dao
 * @Date: 2020-02-01
 */
@Mapper
public interface PerformanceMapper {
    int addPerformanceByPerformance(Performance performance);
    List<Performance> getPerformanceAvgListByPerformance(Performance performance);
}