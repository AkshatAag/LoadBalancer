package com.example.loadBalancer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("CallFromControlLayer")
public class CallFromControlLayer implements Serializable {
    @Id
    private String legId;
    private String conversationId;

    public String getLegId() {
        return legId;
    }

    public void setLegId(String legId) {
        this.legId = legId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public CallFromControlLayer(String legId, String conversationId) {
        this.legId = legId;
        this.conversationId = conversationId;
    }

    public CallFromControlLayer() {
    }
}
