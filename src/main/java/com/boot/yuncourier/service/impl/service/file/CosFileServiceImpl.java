package com.boot.yuncourier.service.impl.service.file;

import com.boot.yuncourier.dao.service.file.CosFileMapper;
import com.boot.yuncourier.entity.service.file.CosFile;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.file.CosFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @ClassName: CosFileServiceImpl
 * @Description: 對象儲存文件service
 * @Date: 2020-02-25
 */

@Component
public class CosFileServiceImpl implements CosFileService {

    @Autowired
    private CosFileMapper cosFileMapper;
    @Override
    public int addCosFileByCosFile(CosFile cosFile) {
        return cosFileMapper.addCosFileByCosFile(cosFile);
    }
    @Override
    public List<CosFile> getCosFileListByUser(User user) {
        return cosFileMapper.getCosFileListByUser(user);
    }
    @Override
    public int deleteCosFileByCosFile(CosFile cosFile) {
        return cosFileMapper.deleteCosFileByCosFile(cosFile);
    }
    @Override
    public CosFile getCosFileInfoByCosFile(CosFile cosFile) {
        return cosFileMapper.getCosFileInfoByCosFile(cosFile);
    }
    @Override
    public int updateCosFileByCosFile(CosFile cosFile) {
        return cosFileMapper.updateCosFileByCosFile(cosFile);
    }
    @Override
    public CosFile getCosFileInfoByKey(CosFile cosFile) {
        return cosFileMapper.getCosFileInfoByKey(cosFile);
    }
    @Override
    public CosFile getCosFileInfoBySign(CosFile cosFile) {
        return cosFileMapper.getCosFileInfoBySign(cosFile);
    }
    @Override
    public int clearCosFileByCosFile(CosFile cosFile) {
        return cosFileMapper.clearCosFileByCosFile(cosFile);
    }
    @Override
    public int getCosFileCountByCosFile(CosFile cosFile) {
        return cosFileMapper.getCosFileCountByCosFile(cosFile);
    }

}
