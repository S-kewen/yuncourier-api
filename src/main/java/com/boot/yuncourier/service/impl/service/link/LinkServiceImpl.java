package com.boot.yuncourier.service.impl.service.link;

import com.boot.yuncourier.dao.service.link.LinkMapper;
import com.boot.yuncourier.entity.service.link.Link;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.service.link.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LinkServiceImpl-我的短連service
 * @Date: 2020-02-01
 */

@Component
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkMapper linkMapper;
    @Override
    public int addLinkByLink(Link link) {
        return linkMapper.addLinkByLink(link);
    }
    @Override
    public List<Link> getLinkListByUser(User user) {
        return linkMapper.getLinkListByUser(user);
    }
    @Override
    public int deleteLinkByLink(Link link) {
        return linkMapper.deleteLinkByLink(link);
    }
    @Override
    public int updateLinkInfoByLink(Link link) {
        return linkMapper.updateLinkInfoByLink(link);
    }
    @Override
    public Link getLinkInfoByLongUrl(Link link) {
        return linkMapper.getLinkInfoByLongUrl(link);
    }
    @Override
    public int deleteLinkByShortUrl(Link link) {
        return linkMapper.deleteLinkByShortUrl(link);
    }
    @Override
    public Link getLinkInfoByLink(Link link) {
        return linkMapper.getLinkInfoByLink(link);
    }
    @Override
    public Link getLinkInfoByShortUrl(Link link) {
        return linkMapper.getLinkInfoByShortUrl(link);
    }
    @Override
    public Link getById(Link link) {
        return linkMapper.getById(link);
    }

}
