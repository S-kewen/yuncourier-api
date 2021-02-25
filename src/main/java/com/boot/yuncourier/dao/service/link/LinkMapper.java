package com.boot.yuncourier.dao.service.link;

import com.boot.yuncourier.entity.service.link.Link;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: LinkMapper-我的短連dao
 * @Date: 2020-02-01
 */
@Mapper
public interface LinkMapper {
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