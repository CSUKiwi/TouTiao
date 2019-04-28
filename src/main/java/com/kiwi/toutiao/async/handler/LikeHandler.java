package com.kiwi.toutiao.async.handler;

import com.kiwi.toutiao.async.EventHandler;
import com.kiwi.toutiao.async.EventModel;
import com.kiwi.toutiao.async.EventType;
import com.kiwi.toutiao.model.Message;
import com.kiwi.toutiao.model.User;
import com.kiwi.toutiao.service.MessageService;
import com.kiwi.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Kiwi
 * @date 2019/4/28 13:27
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;


    @Override
    public void doHandle(EventModel model) {
        //System.out.println("Liked");
        Message message = new Message();
        message.setFromId(3);
        //为了演示效果，给自己发一个消息。
        message.setToId(model.getActorId());
        User user = userService.getUser(model.getActorId());
        message.setToId(model.getEntityOwnerId());
        message.setContent("用户" + user.getName() +
                "赞了你的资讯,http://127.0.0.1:8080/news/"
        +String.valueOf(model.getEntityId()));

        //SYSTEM ACCOUNT
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    /**只关心点赞这个事*/
    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
