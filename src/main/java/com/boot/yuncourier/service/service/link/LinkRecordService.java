package com.boot.yuncourier.service.service.link;

import com.boot.yuncourier.entity.service.link.LinkRecord;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LinkRecordService-短連轉發記錄接口
 * @Date: 2020-02-01
 */

public interface LinkRecordService {

    int addLinkRecordByLinkRecord(LinkRecord linkRecord);
    List<LinkRecord> getLinkRecordListByUser(User user);
    int deleteLinkRecordByLinkRecord(LinkRecord linkRecord);
    LinkRecord getLinkRecordInfoByLinkRecord(LinkRecord linkRecord);
    LinkRecord getLinkRecordInfoByIp(LinkRecord linkRecord);
    LinkRecord getById(LinkRecord linkRecord);
}
