package com.boot.yuncourier.dao.service.file;

import com.boot.yuncourier.entity.service.file.CosFile;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @ClassName: CosFileMapper
 * @Description: 對象儲存文件dao
 * @Date: 2020-02-25
 */
@Mapper
public interface CosFileMapper {
    int addCosFileByCosFile(CosFile Mail);
    List<CosFile> getCosFileListByUser(User user);
    int deleteCosFileByCosFile(CosFile cosFile);
    CosFile getCosFileInfoByCosFile(CosFile cosFile);
    int updateCosFileByCosFile(CosFile cosFile);
    CosFile getCosFileInfoByKey(CosFile cosFile);
    CosFile getCosFileInfoBySign(CosFile cosFile);
    int clearCosFileByCosFile(CosFile cosFile);
    int getCosFileCountByCosFile(CosFile cosFile);
}