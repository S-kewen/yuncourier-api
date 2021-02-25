package com.boot.yuncourier.service.service.link;

import com.boot.yuncourier.entity.service.link.Link;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LinkService-我的短連接口
 * @Date: 2020-02-01
 */

public interface LinkService {

    int addLinkByLink(Link link);
    List<Link> getLinkListByUser(User user);
    int deleteLinkByLink(Link link);
    int updateLinkInfoByLink(Link link);
    Link getLinkInfoByLongUrl(Link link);
    int deleteLinkByShortUrl(Link link);
    Link getLinkInfoByLink(Link link);
    Link getLinkInfoByShortUrl(Link link);
    Link getById(Link link);
}
