package com.kiwi.toutiao.service;

import com.kiwi.toutiao.dao.CommentDAO;
import com.kiwi.toutiao.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kiwi
 * @date 2019/4/26 16:47
 * 评论service
 */
@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    CommentDAO commentDAO;

    /**得到评论列表*/
    public List<Comment> getCommentByEntity(int entityId, int entityType){
        return commentDAO.selectByEntity(entityId, entityType);
    }

    /**增加评论*/
    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    /**得到评论数*/
    public int getCommentCount(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }

    /**删除评论*/
    public void deleteComment(int entityId, int entityType){
        commentDAO.updateStatus(entityId, entityType, 1);
    }
}
