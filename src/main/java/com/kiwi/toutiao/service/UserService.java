package com.kiwi.toutiao.service;

import com.kiwi.toutiao.dao.LoginTicketDAO;
import com.kiwi.toutiao.dao.UserDAO;
import com.kiwi.toutiao.model.LoginTicket;
import com.kiwi.toutiao.model.User;
import com.kiwi.toutiao.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Kiwi
 * @date 2019/4/16 15:25
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    /**用于注册用户*/
    public Map<String, Object> register(String username, String password){
        Map<String, Object> map = new HashMap<String, Object>();

        if (StringUtils.isBlank(username)){
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)){
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user != null){
            map.put("msgname", "用户名已经被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    /**用于登陆用户*/
    public Map<String, Object> login(String username, String password){
        Map<String, Object> map = new HashMap<String, Object>();

        if (StringUtils.isBlank(username)){
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)){
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user == null){
            map.put("msgname", "用户名不存在");
            return map;
        }

        if (!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd", "密码不正确");
            return map;
        }

        map.put("userId", user.getId());
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    /**用于记录登录状态*/
    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        //设置可登录时长
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        //设置0为有效,可登录条件之一
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        //登陆态设置
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }


   /**用于得到用户*/
    public User getUser(int id){
        return userDAO.selectById(id);
    }

    /**用于登出*/
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket, 1);
    }

}
