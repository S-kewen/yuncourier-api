package com.boot.yuncourier.service.user;


import com.boot.yuncourier.entity.user.News;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: NewsService-系統消息接口
 * @Date: 2020-02-01
 */

public interface NewsService {
    int addNewsByNews (News news);
    int getCountByNews(News news);
    List<News> getNewsListByNews(User user);
    int deleteNewsByNews(News news);
    int updateNewsByNews(News news);
    int allReadByNews(User user);
    List<News> getUserIndexNewsListByUser(User user);
}
