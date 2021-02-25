package com.boot.yuncourier.dao.app;

import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: SoftwareMapper-我的應用dao
 * @Date: 2020-02-01
 */
@Mapper
public interface SoftwareMapper {
    Software getSoftwareInfoBySoftware(Software Software);
    Software checkBySoftware(Software Software);
    List<Software> getSoftwareListByUser(User user);
    int deleteSoftwareBySoftware(Software software);
    int checkSoftwareNameBySoftware(Software software);
    int addSoftware(Software software);
    int updateSoftwareInfoBySoftware(Software software);
}