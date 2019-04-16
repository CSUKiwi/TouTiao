package com.kiwi.toutiao.service;

import com.kiwi.toutiao.dao.NewsDAO;
import com.kiwi.toutiao.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kiwi
 * @date 2019/4/16 15:27
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

//    用于展示新的消息
    public List<News> getLatestNews(int userId, int offset, int limit){
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

}
