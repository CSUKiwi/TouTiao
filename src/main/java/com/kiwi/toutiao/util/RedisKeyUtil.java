package com.kiwi.toutiao.util;

/**
 * @author Kiwi
 * @date 2019/4/27 19:11
 * Redis编号生成器。
 * 根据规范来生成key，前两个字符表示业务，后两个字符表示参数。
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";

    public static String getEventQueueKey(){
        return BIZ_EVENT;
    }

    /**喜欢的Key*/
    public static String getLikeKey(int entityId, int entityType){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    /**不喜欢的Key*/
    public static String getDisLikeKey(int entityId, int entityType){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
}
