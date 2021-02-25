package com.boot.yuncourier.task;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: skwen
 * @Description: SinglethreadScheduleTask-單線程任務
 * @Date: 2020-02-01
 */

@Component
@EnableScheduling // 1.开启定时任务
//@EnableAsync        // 不开启多线程
public class SinglethreadScheduleTask {

//    @Async
//    @Scheduled(fixedDelay = 2000)
//    public void second() {
//        System.out.println("第二个定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
//        System.out.println();
//    }

}
