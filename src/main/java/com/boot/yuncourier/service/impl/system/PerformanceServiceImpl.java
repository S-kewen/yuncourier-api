package com.boot.yuncourier.service.impl.system;

import com.boot.yuncourier.dao.system.PerformanceMapper;
import com.boot.yuncourier.entity.system.Performance;
import com.boot.yuncourier.service.system.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: PerformanceServiceImpl-系統性能負載service
 * @Date: 2020-02-01
 */

@CacheConfig(cacheNames = "performance")// 定义缓存的名字，原则上是必须的
@Component
public class PerformanceServiceImpl implements PerformanceService {
    @Autowired
    private PerformanceMapper performanceMapper;
    @Override
    @CachePut(key="#performance.add_time")
    public int addPerformanceByPerformance(Performance performance) {
        return performanceMapper.addPerformanceByPerformance(performance);
    }
    @Override
    @Cacheable(key="#performance.add_time")//这个是在在查询时使用的缓存注解，一定要定义key值，唯一的。
    public List<Performance> getPerformanceAvgListByPerformance(Performance performance) {
        return performanceMapper.getPerformanceAvgListByPerformance(performance);
    }
}
