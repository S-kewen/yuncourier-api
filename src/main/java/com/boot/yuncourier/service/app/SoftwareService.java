package com.boot.yuncourier.service.app;

import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: SoftwareService-我的應用接口
 * @Date: 2020-02-01
 */

public interface SoftwareService {
    Software getSoftwareInfoBySoftware(Software Software);
    Software checkBySoftware (Software Software);
    List<Software> getSoftwareListByUser(User user);
    int deleteSoftwareBySoftware(Software software);
    int checkSoftwareNameBySoftware(Software software);
    int addSoftware(Software software);
    int updateSoftwareInfoBySoftware(Software software);
}
