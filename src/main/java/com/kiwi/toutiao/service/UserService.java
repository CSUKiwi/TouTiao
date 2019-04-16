package com.kiwi.toutiao.service;

import com.kiwi.toutiao.dao.UserDAO;
import com.kiwi.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kiwi
 * @date 2019/4/16 15:25
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

//    用于得到用户
    public User getUser(int id){
        return userDAO.selectById(id);
    }

}
