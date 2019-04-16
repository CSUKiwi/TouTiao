package com.kiwi.toutiao.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Kiwi
 * @date 2019/4/16 20:06
 * 专门用来记录log，记录所有的参数，用于对函数进行监控。
 * 面向切面编程。
 */
@Aspect
@Component
public class LogAspect {
//    用于写日志。
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

//    执行所以controller方法前都要调用该方法，（）里面用来指定目标，前面的则为正则表达式。
    @Before("execution(* com.kiwi.toutiao.controller.*Controller.*(..))")
//    joinPoint为切点，用来打印单个参数。
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()){
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("before method: " + sb.toString());
    }

//    执行indexcontroller方法后都调用该方法.
    @After("execution(* com.kiwi.toutiao.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint){
        logger.info("after method: ");
    }
}
