package com.kiwi.toutiao.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kiwi
 * @date 2019/4/16 15:21
 * 视图对象，专门用来给视图做展示的一个类。
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();
    public void set(String key, Object value){
        objs.put(key, value);
    }
    public Object get(String key){
        return objs.get(key);
    }
}
