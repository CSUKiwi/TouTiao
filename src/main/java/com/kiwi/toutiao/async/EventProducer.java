package com.kiwi.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import com.kiwi.toutiao.util.JedisAdapter;
import com.kiwi.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kiwi
 * @date 2019/4/28 13:38
 * 事件生产者
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    /**把事件放到队列里面*/
    public boolean fireEvent(EventModel eventModel){
        try{
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
