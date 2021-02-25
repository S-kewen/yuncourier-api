package com.boot.yuncourier.service.service.file;

import com.boot.yuncourier.entity.service.file.CosFile;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @ClassName: 對象儲存文件接口
 * @Description: CosFileService
 * @Date: 2020-02-25
 */

public interface CosFileService {

    int addCosFileByCosFile(CosFile cosFile);
    List<CosFile> getCosFileListByUser(User user);
    int deleteCosFileByCosFile(CosFile cosFile);
    CosFile getCosFileInfoByCosFile(CosFile cosFile);
    int updateCosFileByCosFile(CosFile cosFile);
    CosFile getCosFileInfoByKey(CosFile cosFile);
    CosFile getCosFileInfoBySign(CosFile cosFile);
    int clearCosFileByCosFile(CosFile cosFile);
    int getCosFileCountByCosFile(CosFile cosFile);
}
