package com.boot.yuncourier.service.impl.app;

import com.boot.yuncourier.dao.app.SoftwareMapper;
import com.boot.yuncourier.entity.app.Software;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.app.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: SoftwareServiceImpl-我的應用service
 * @Date: 2020-02-01
 */

@Component
public class SoftwareServiceImpl implements SoftwareService {

    @Autowired
    private SoftwareMapper SoftwareMapper;
    @Override
    public Software getSoftwareInfoBySoftware(Software Software) {
        return SoftwareMapper.getSoftwareInfoBySoftware(Software);
    }
    @Override
    public Software checkBySoftware(Software Software) {
        return SoftwareMapper.checkBySoftware(Software);
    }
    @Override
    public List<Software> getSoftwareListByUser(User user) {
        return SoftwareMapper.getSoftwareListByUser(user);
    }
    @Override
    public int deleteSoftwareBySoftware(Software software) {
        return SoftwareMapper.deleteSoftwareBySoftware(software);
    }
    @Override
    public int checkSoftwareNameBySoftware(Software software) {
        return SoftwareMapper.checkSoftwareNameBySoftware(software);
    }
    @Override
    public int addSoftware(Software software) {
        return SoftwareMapper.addSoftware(software);
    }
    @Override
    public int updateSoftwareInfoBySoftware(Software software) {
        return SoftwareMapper.updateSoftwareInfoBySoftware(software);
    }


}
