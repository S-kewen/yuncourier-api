package com.boot.yuncourier.util;

import java.util.Map;

/**
 * @Author: skwen
 * @ClassName: TencentCloudCosUtil
 * @Description: 騰訊雲cos工具實現類接口
 * @Date: 2020-02-29
 */
public interface TencentCloudCosUtil  {
    Map<String, Object> getObjectUrl(String bucketName, String region, String key, String secretId, String secretKey);
}
