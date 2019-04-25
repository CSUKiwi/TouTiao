package com.kiwi.toutiao.configuration;

import com.kiwi.toutiao.interceptor.LoginRequiredInterceptor;
import com.kiwi.toutiao.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Kiwi
 * @date 2019/4/25 20:06
 * 用于将拦截器注册进去.
 * @Component为初始化.
 */
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        //增加需要权限的页面
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/msg/*")
                .addPathPatterns("/like").addPathPatterns("/dislike");
        super.addInterceptors(registry);
    }
}
