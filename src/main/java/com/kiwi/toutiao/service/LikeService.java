package com.kiwi.toutiao.service;

import com.kiwi.toutiao.util.JedisAdapter;
import com.kiwi.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kiwi
 * @date 2019/4/27 19:42
 * 没有model,没有dao,直接从JedisAdapter(Util)中调用
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;

    /**判断某个用户对某一项元素是否喜欢，喜欢返回1，不喜欢返回-1，否则返回0*/
    public int getLikeStatus(int userId, int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId)))
            return 1;
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    /**判断有多少人喜欢*/
    public long like(int userId, int entityType, int entityId){
        //在喜欢的集合里增加
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        //在不喜欢的集合里删除
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));
        //返回当前有多少人喜欢
        return jedisAdapter.scard(likeKey);
    }

    /**判断有多少人不喜欢*/
    public long disLike(int userId, int entityType, int entityId){
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }


}
