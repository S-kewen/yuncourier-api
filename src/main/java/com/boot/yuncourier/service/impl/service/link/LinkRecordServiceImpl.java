package com.boot.yuncourier.service.impl.service.link;

import com.boot.yuncourier.dao.service.link.LinkRecordMapper;
import com.boot.yuncourier.entity.service.link.LinkRecord;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.link.LinkRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LinkRecordServiceImpl-短連轉發記錄service
 * @Date: 2020-02-01
 */

@Component
public class LinkRecordServiceImpl implements LinkRecordService {

    @Autowired
    private LinkRecordMapper linkRecordMapper;
    @Override
    public int addLinkRecordByLinkRecord(LinkRecord linkRecord) {
        return linkRecordMapper.addLinkRecordByLinkRecord(linkRecord);
    }
    @Override
    public List<LinkRecord> getLinkRecordListByUser(User user) {
        return linkRecordMapper.getLinkRecordListByUser(user);
    }
    @Override
    public int deleteLinkRecordByLinkRecord(LinkRecord linkRecord) {
        return linkRecordMapper.deleteLinkRecordByLinkRecord(linkRecord);
    }
    @Override
    public LinkRecord getLinkRecordInfoByLinkRecord(LinkRecord linkRecord) {
        return linkRecordMapper.getLinkRecordInfoByLinkRecord(linkRecord);
    }
    @Override
    public LinkRecord getLinkRecordInfoByIp(LinkRecord linkRecord) {
        return linkRecordMapper.getLinkRecordInfoByIp(linkRecord);
    }
    @Override
    public LinkRecord getById(LinkRecord linkRecord) {
        return linkRecordMapper.getById(linkRecord);
    }

}
