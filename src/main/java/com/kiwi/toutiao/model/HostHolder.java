package com.kiwi.toutiao.model;

import org.springframework.stereotype.Component;

/**
 * @author Kiwi
 * @date 2019/4/25 19:38
 * 用于存储当前用户是谁.
 * 使用了线程本地变量,多个用户使用,各存各的.
 * @component:用于初始化.
 */
@Component
public class HostHolder {

    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
