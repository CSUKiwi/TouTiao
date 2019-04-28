package com.kiwi.toutiao.async;

import java.util.List;

/**
 * @author Kiwi
 * @date 2019/4/28 13:25
 * Event统一的接口
 */
public interface EventHandler {

    /**每个handler要处理的事情，这个方法需要关注某一些事件类型*/
    void doHandle(EventModel model);

    /**这个handler需要关注那些type*/
    List<EventType> getSupportEventTypes();
}
