package com.kiwi.toutiao.async;

/**
 * @author Kiwi
 * @date 2019/4/28 13:11
 * 事件枚举类，列出有多少种类型
 */
public enum EventType {
    //各种类型的枚举类
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private  int value;

    EventType(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
