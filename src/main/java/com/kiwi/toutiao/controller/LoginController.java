package com.kiwi.toutiao.controller;


import com.kiwi.toutiao.service.UserService;
import com.kiwi.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Kiwi
 * @date 2019/4/25 14:10
 * 用户登录注册
 */
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;


    /**用于注册*/
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberme,
                      HttpServletResponse response){
        try{
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("ticket")){
                //如果成功,将ticket写入cookie里面.
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                //设置路径为全站有效
                cookie.setPath("/");
                if (rememberme > 0){
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }
    }

    /**登陆*/
    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember", defaultValue = "0") int rememberme,
                        HttpServletResponse response){
        try{
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "登陆成功");
            } else {
                return ToutiaoUtil.getJSONString(1, "登陆异常");
            }
        } catch (Exception e){
            logger.error("登陆异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登陆异常");
        }
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
}
