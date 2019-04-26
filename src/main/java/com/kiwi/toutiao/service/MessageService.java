package com.kiwi.toutiao.service;

import com.kiwi.toutiao.dao.MessageDAO;
import com.kiwi.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kiwi
 * @date 2019/4/26 18:41
 * 消息Service
 * conversation的总条数存在id里面。
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    /**用于增加评论*/
    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    /**用于得到消息队列*/
    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    /**用于得到消息细节*/
    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    /**用于得到未读消息数*/
    public int getConversationUnreadCount(int userId, String conversationId){
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }
}
