package com.kiwi.toutiao.controller;

import com.kiwi.toutiao.model.EntityType;
import com.kiwi.toutiao.model.HostHolder;
import com.kiwi.toutiao.model.News;
import com.kiwi.toutiao.model.ViewObject;
import com.kiwi.toutiao.service.LikeService;
import com.kiwi.toutiao.service.NewsService;
import com.kiwi.toutiao.service.UserService;
import org.omg.PortableInterceptor.HOLDING;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kiwi
 * @date 2019/4/16 15:37
 * 主页Controller
 */
@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    /**用于给视图做展示*/
    private List<ViewObject> getNews(int userId, int offset, int limit){
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for (News news : newsList){
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            if (localUserId != 0)
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            else
                vo.set("like", 0);
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/", "/index"},method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop",defaultValue = "0") int pop){
        model.addAttribute("vos", getNews(0, 0, 10));
        if (hostHolder.getUser() != null)
            pop = 0;
        //用于与前端交互弹出登录框.
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"},method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }

}
