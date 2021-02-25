package com.boot.yuncourier.dao.service.link;

import com.boot.yuncourier.entity.service.link.LinkRecord;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LinkRecordMapper-短連轉發記錄dao
 * @Date: 2020-02-01
 */
@Mapper
public interface LinkRecordMapper {
    int addLinkRecordByLinkRecord(LinkRecord linkRecord);
    List<LinkRecord> getLinkRecordListByUser(User user);
    int deleteLinkRecordByLinkRecord(LinkRecord linkRecord);
    LinkRecord getLinkRecordInfoByLinkRecord(LinkRecord linkRecord);
    LinkRecord getLinkRecordInfoByIp(LinkRecord linkRecord);
    LinkRecord getById(LinkRecord linkRecord);
}