package com.boot.yuncourier.service.impl.user;

import com.boot.yuncourier.dao.user.NewsMapper;
import com.boot.yuncourier.entity.user.News;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.user.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: NewsServiceImpl-系統消息service
 * @Date: 2020-02-01
 */

@Component
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;
    @Override
    public int addNewsByNews(News news) {
        return newsMapper.addNewsByNews(news);
    }
    @Override
    public int getCountByNews(News news) {
        return newsMapper.getCountByNews(news);
    }
    @Override
    public List<News> getNewsListByNews(User user) {
        return newsMapper.getNewsListByNews(user);
    }
    @Override
    public int deleteNewsByNews(News news) {
        return newsMapper.deleteNewsByNews(news);
    }
    @Override
    public int updateNewsByNews(News news) {
        return newsMapper.updateNewsByNews(news);
    }
    @Override
    public int allReadByNews(User user) {
        return newsMapper.allReadByNews(user);
    }
    @Override
    public List<News> getUserIndexNewsListByUser(User user) {
        return newsMapper.getUserIndexNewsListByUser(user);
    }

}
