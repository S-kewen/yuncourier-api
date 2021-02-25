package com.boot.yuncourier.service.system;

import com.boot.yuncourier.entity.system.Performance;

import java.util.List;

/**
 * @Author: skwen
 * @Description: PerformanceService-系統性能負載接口
 * @Date: 2020-02-01
 */

public interface PerformanceService {
    int addPerformanceByPerformance(Performance performance);
    List<Performance> getPerformanceAvgListByPerformance(Performance performance);
}
