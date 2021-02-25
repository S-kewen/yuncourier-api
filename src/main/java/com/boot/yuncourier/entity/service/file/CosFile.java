package com.boot.yuncourier.entity.service.file;

import lombok.Data;

import java.util.Date;

/**
 * @Author: skwen
 * @ClassName: CosFile
 * @Description: 對象儲存文件實體類
 * @Date: 2020-02-25
 */

@Data
public class CosFile {
    private int id;
    private int user_id;
    private int cos_id;
    private String file_name;
    private String password;
    private String md5;
    private long size;
    private String bucket;
    private String file_path;
    private String key;
    private String region;
    private String request_id;
    private int state;
    private int file_type;
    private int open_type;
    private int deposit_type;
    private Date expire_time;
    private String sign;
    private String remark;
    private String download_url;
    private String cos_url;
    private String short_url;
    private Boolean deleted;
    private Date add_time;
    //以上來自sql
    private String cos_name;
    private String secret_id;
    private String secret_key;
}
