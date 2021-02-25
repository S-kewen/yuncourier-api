package com.boot.yuncourier.dao.user;


import com.boot.yuncourier.entity.user.News;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: NewsMapper-系統消息dao
 * @Date: 2020-02-01
 */
@Mapper
public interface NewsMapper {
    int addNewsByNews(News news);
    int getCountByNews(News news);
    List<News> getNewsListByNews(User user);
    int deleteNewsByNews(News news);
    int updateNewsByNews(News news);
    int allReadByNews(User user);
    List<News> getUserIndexNewsListByUser(User user);
}