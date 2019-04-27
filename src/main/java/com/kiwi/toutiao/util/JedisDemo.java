package com.kiwi.toutiao.util;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * @author Kiwi
 * @date 2019/4/27 18:39
 * 用于展示Redis在Java中的应用：Jedis。
 */
public class JedisDemo {

    /**用于展示效果*/
    public static void print(int index, Object obj) {
        System.out.println(String.format("%d,%s", index, obj.toString()));
    }

    public static void main(String[] argv){

        Jedis jedis = new Jedis();
        //删除数据库
        jedis.flushAll();
        //get，set
        jedis.set("hello", "world");
        print(1,jedis.get("hello"));
        //重命名
        jedis.rename("hello","newhello");
        print(1,jedis.get("newhello"));
        //设置过期时间
        jedis.setex("hello2",15,"world");


        /**数值操作*/
        jedis.set("pv","100");
        //增加
        jedis.incr("pv");
        print(2,jedis.get("pv"));
        //步进减少
        jedis.decrBy("pv",5);
        //步进增加
        jedis.incrBy("pv",7);
        print(2,jedis.get("pv"));
        print(3, jedis.keys("*"));

        /**列表操作，最近来访，粉丝列表，消息队列*/
        String listName = "list";
        jedis.del(listName);
        //从左边依次推进。
        for(int i = 0; i < 10; ++i){
            jedis.lpush(listName, "a"+String.valueOf(i));
        }
        //列表的显示
        print(4,jedis.lrange(listName,0,12));
        //显示长度
        print(5,jedis.llen(listName));
        //推出一个元素
        print(6,jedis.lpop(listName));
        print(7,jedis.llen(listName));
        // 最近来访5个id
        print(8, jedis.lrange(listName, 2, 6));
        //显示下标为3的元素
        print(9,jedis.lindex(listName,3));
        //把xx插入a4后面
        print(10,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a4","xx"));
        //把bb插入a4前面
        print(10,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"a4","bb"));
        print(11,jedis.lrange(listName,0,12));


        /**不定的属性用hash比较好，可加可减。可变字段*/
        String userKey = "userxx";
        jedis.hset(userKey,"name","jim");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","1866666666");
        print(12,jedis.hget(userKey,"name"));
        print(13,jedis.hgetAll(userKey));
        //删除字段
        jedis.hdel(userKey,"phone");
        print(14,jedis.hgetAll(userKey));
        //获取所有关键字
        print(15,jedis.hkeys(userKey));
        //获取所有值
        print(16,jedis.hvals(userKey));
        //判断是否存在
        print(17,jedis.hexists(userKey,"email"));
        print(18,jedis.hexists(userKey,"age"));
        //判断是否存在，如果存在就不变，不存在就添加
        jedis.hsetnx(userKey,"school","zju");
        jedis.hsetnx(userKey,"name","lsy");
        print(19,jedis.hgetAll(userKey));



        /**set集合，点赞用户群，共同好友，共同关注等*/
        String likeKeys1 = "newsLike1";
        String likeKeys2 = "newsLike2";
        for(int i = 0; i < 10; ++i){
            jedis.sadd(likeKeys1,String.valueOf(i));
            jedis.sadd(likeKeys2,String.valueOf(i*2));
        }
        //把likeKey1打印出来
        print(20,jedis.smembers(likeKeys1));
        print(21,jedis.smembers(likeKeys2));
        //求交
        print(22,jedis.sinter(likeKeys1,likeKeys2));
        //求并
        print(23,jedis.sunion(likeKeys1,likeKeys2));
        //我有你没有的
        print(24,jedis.sdiff(likeKeys1,likeKeys2));
        //判断集合中是否存在某种元素
        print(25,jedis.sismember(likeKeys1,"5"));
        //删除集合中的一个元素
        jedis.srem(likeKeys1,"5");
        print(26,jedis.smembers(likeKeys1));
        //查看元素有多所少个值
        print(27,jedis.scard(likeKeys1));
        //将2中的14移到1中
        jedis.smove(likeKeys2,likeKeys1,"14");
        print(28,jedis.smembers(likeKeys1));
        //判断长度
        print(29,jedis.scard(likeKeys1));


        /**排序集合，有限队列，排行榜*/
        String rankKey = " rankKey";
        //除了添加key外，还要额外带一个分值。
        jedis.zadd(rankKey, 15, "Jim");
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Lee");
        jedis.zadd(rankKey, 75, "Lucy");
        jedis.zadd(rankKey, 80, "Mei");
        //查看有几个值
        print(30, jedis.zcard(rankKey));
        //查看范围里面的值
        print(31, jedis.zcount(rankKey, 61, 100));
        // 改错卷了
        //查看某个人的分值
        print(32, jedis.zscore(rankKey, "Lucy"));
        //给lucy的成绩提高两分
        jedis.zincrby(rankKey, 2, "Lucy");
        print(33, jedis.zscore(rankKey, "Lucy"));
        //给一个不存在的人增加两分。
        jedis.zincrby(rankKey, 2, "Luc");
        print(34, jedis.zscore(rankKey, "Luc"));
        print(35, jedis.zcount(rankKey, 0, 100));
        // 1-4 名 Luc
        print(36, jedis.zrange(rankKey, 0, 10));
        //排序，这里是从小到大排序
        print(36, jedis.zrange(rankKey, 1, 3));
        //从大到小排序
        print(36, jedis.zrevrange(rankKey, 1, 3));
        //把元素和分值打出来（按照排序来）
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")) {
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }
        //查看排名（从0开始）
        print(38, jedis.zrank(rankKey, "Ben"));
        //从大到小查看排名
        print(39, jedis.zrevrank(rankKey, "Ben"));

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");
        print(40, jedis.zlexcount(setKey, "-", "+"));
        print(41, jedis.zlexcount(setKey, "(b", "[d"));
        print(42, jedis.zlexcount(setKey, "[b", "[d"));
        jedis.zrem(setKey, "b");
        print(43, jedis.zrange(setKey, 0, 10));
        jedis.zremrangeByLex(setKey, "(c", "+");
        print(44, jedis.zrange(setKey, 0, 2));


        jedis.lpush("aaa", "A");
        jedis.lpush("aaa", "B");
        jedis.lpush("aaa", "C");
        print(45, jedis.brpop(0, "aaa"));
        print(45, jedis.brpop(0, "aaa"));
        print(45, jedis.brpop(0, "aaa"));

        /**就像线程池一样。*/
        JedisPool pool = new JedisPool();
        for (int i = 0; i < 8; ++i) {
            Jedis j = pool.getResource();
            j.get("a");
            System.out.println("POOL" + i);
            j.close();
        }
    }
}
