package com.kiwi.toutiao.async;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kiwi
 * @date 2019/4/28 13:14
 * 放到队列里面，表示刚刚发生了什么事情，信息。
 * type表示事件类型，actorId表示事件触发者，
 * entityId表示事件触发对象，entityOwnerId表示事件对象的拥有者。
 */
public class EventModel {

    private EventType type;
    private int actorId;
    private int entityId;
    private int entityType;
    private int entityOwnerId;
    private Map<String, String> exts = new HashMap<>();

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel() {

    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public String getExt(String name) {
        return exts.get(name);
    }

    public EventModel setExt(String name, String value) {
        exts.put(name, value);
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }
}