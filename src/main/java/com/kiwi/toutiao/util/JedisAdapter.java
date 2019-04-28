package com.kiwi.toutiao.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @author Kiwi
 * @date 2019/4/27 18:55
 * 用于Redis在Java中的应用Jedis。
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private Jedis jedis = null;
    private JedisPool pool = null;

    /**对象在初始化完了以后需要把这个pool初始化*/
    @Override
    public void afterPropertiesSet() throws Exception {
        //jedis = new Jedis("localhost");
        pool = new JedisPool("localhost", 6379);
    }

    /**用于获取Jedis*/
    private Jedis getJedis(){
        //return jedis;
        return pool.getResource();
    }

    public String get(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public void set(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**点赞*/
    public long sadd(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**取消点赞*/
    public long srem(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key, value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**判断是否点赞成功*/
    public boolean sismember(String key, String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**判断点赞集合中有多少人*/
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**验证码，防止机器注册，记录上次注册时间，有效期为3天*/
    public void setex(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.setex(key, 10, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**将事件放到队列里面去*/
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**直接存储一个对象，用于队列*/
    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    /**取出对象，用于队列*/
    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null)
            return JSON.parseObject(value, clazz);
        return null;
    }



}
