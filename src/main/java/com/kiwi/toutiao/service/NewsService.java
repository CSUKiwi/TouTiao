package com.kiwi.toutiao.service;

import com.kiwi.toutiao.dao.NewsDAO;
import com.kiwi.toutiao.model.News;
import com.kiwi.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * @author Kiwi
 * @date 2019/4/16 15:27
 * 新闻Service
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    /**用于展示新的消息*/
    public List<News> getLatestNews(int userId, int offset, int limit){
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    /**用于保存图片*/
    public String saveImage(MultipartFile file) throws IOException{
        //寻找最后一个点的位置，来判断图片格式对不对
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0)
            return null;

        //判断图片格式是否符合要求。
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (ToutiaoUtil.isFileAllowed(fileExt))
            return null;

        String fileName = UUID.randomUUID().toString().
                replaceAll("-","") +"."+fileExt;
        //如果图片存在就替换掉
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR +
                fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        //可以通过前端页面访问图片
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }

    /**用于增加消息*/
    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    /**寻找新闻*/
    public News getById(int newsId){
        return newsDAO.getById(newsId);
    }

    /**更新评论数*/
    public int updateCommentCount(int id, int count){
        return newsDAO.updateCommentCount(id, count);
    }

    /**更新喜欢数*/
    public int updateLikeCount(int id, int count){
        return newsDAO.updateLikeCount(id, count);
    }
}
