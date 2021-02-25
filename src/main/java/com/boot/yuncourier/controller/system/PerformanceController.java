package com.boot.yuncourier.controller.system;

import com.boot.yuncourier.service.system.PerformanceService;
import com.boot.yuncourier.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: skwen
 * @Description: PerformanceController-系統性能負載
 * @Date: 2020-01-31
 */
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://courier.iskwen.com", maxAge = 3600)
public class PerformanceController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PerformanceService performanceService;
    @Autowired
    private TokenUtil tokenUtil;


}
