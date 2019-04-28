package com.kiwi.toutiao.controller;

import com.kiwi.toutiao.async.EventModel;
import com.kiwi.toutiao.async.EventProducer;
import com.kiwi.toutiao.async.EventType;
import com.kiwi.toutiao.model.EntityType;
import com.kiwi.toutiao.model.HostHolder;
import com.kiwi.toutiao.model.News;
import com.kiwi.toutiao.service.LikeService;
import com.kiwi.toutiao.service.NewsService;
import com.kiwi.toutiao.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Kiwi
 * @date 2019/4/27 20:28
 * 点赞点踩Controller
 */
@Controller
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    /**喜欢*/
    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newsId") int newsId){
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);

        //更新喜欢数
        News news = newsService.getById(newsId);
        newsService.updateCommentCount(newsId, (int)likeCount);

        //把当时的情况记录下来,利用队列。
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                    .setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
        .setEntityType(EntityType.ENTITY_NEWS).setEntityOwnerId(news.getUserId()));
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    /**不喜欢*/
    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newsId") int newsId){
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
        //更新喜欢数
        newsService.updateLikeCount(newsId, (int)likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }



}
