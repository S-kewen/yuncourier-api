package com.boot.yuncourier.entity.service.file;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @ClassName: TencentCos
 * @Description: 騰訊雲cos實體類
 * @Date: 2020-02-23
 */

@Data
public class TencentCos {
    private int id;
    private int user_id;
    private int state;
    private int software_id;
    private String cos_name;
    private String default_path;
    private String bucket_name;
    private int region;
    private String secret_id;
    private String secret_key;
    private String remark;
    private boolean deleted;
    private Date add_time;
    //以上是sql字段
    private String software_name;
}
