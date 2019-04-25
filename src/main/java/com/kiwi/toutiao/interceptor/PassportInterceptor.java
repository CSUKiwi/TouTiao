package com.kiwi.toutiao.interceptor;

import com.kiwi.toutiao.dao.LoginTicketDAO;
import com.kiwi.toutiao.dao.UserDAO;
import com.kiwi.toutiao.model.HostHolder;
import com.kiwi.toutiao.model.LoginTicket;
import com.kiwi.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author Kiwi
 * @date 2019/4/25 19:42
 * 全局拦截器,判断该用户是否为登陆用户.
 * 链路回调思想
 */
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        //用户的数据存在cookie里面,遍历,判断是否有cookie.
        if (httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()){
                if (cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        //查看ticket是否处于有效期,防止ticket是伪造的.
        if (ticket != null){
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                return true;
            }

            //用于临时保存用户信息,表示当前用户是谁.
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //后台与前端交互.
        if (modelAndView != null && hostHolder.getUser() != null){
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //清除用户信息.
        hostHolder.clear();
    }
}
