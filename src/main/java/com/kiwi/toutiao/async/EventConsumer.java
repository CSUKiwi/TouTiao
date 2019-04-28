package com.kiwi.toutiao.async;

import com.alibaba.fastjson.JSON;
import com.kiwi.toutiao.util.JedisAdapter;
import com.kiwi.toutiao.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kiwi
 * @date 2019/4/28 13:43
 * 事件消费者
 * ApplicationContextAware用于记录所有的事件
 */
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private JedisAdapter jedisAdapter;

    /**通过event找到对应需要处理的handler，然后一个一个执行*/
    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    private ApplicationContext applicationContext;

    /**把当前所有实现了eventHandler的类都找出来做一个表处理*/
    @Override
    public void afterPropertiesSet() throws Exception {
        //把当前类中所有实现eventHandler接口的类都找出来
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null){
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()){
                //看EventHandler需要注册那些Type，把每个eventType都取出来
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type : eventTypes){
                    //如果没有type。需要put一个新的type进去。
                    if (!config.containsKey(type))
                        config.put(type, new ArrayList<EventHandler>());

                    //注册每个事件的处理函数
                    config.get(type).add(entry.getValue());
                }
            }
        }

        /**启动线程去消费事件。
         * 一直取数据，找出来数据，分别用哪几个handler来处理*/
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                //从队列一直消费，死循环
                while(true){
                    //把插进去的队列从右边取出来
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> messages = jedisAdapter.brpop(0, key);

                    //第一个元素是队列名字。
                    for (String message : messages){
                        if (message.equals(key))
                            continue;

                        //反序列化
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);

                        //找到这个事件的处理handler列表
                        if (!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件");
                            continue;
                        }

                        /**让每个handler去处理*/
                        for (EventHandler handler : config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    /**可以把applicationContext记录下来*/
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
