package com.duansky.hazelcast.graphflow.components.event;

import com.duansky.hazelcast.graphflow.components.Event;
import com.duansky.hazelcast.graphflow.graph.Vertex;

/**
 * Created by SkyDream on 2017/3/15.
 */
public class VertexEvent<KV,VV> implements Event<EventType,Vertex<KV,VV>> {

    private EventType type;
    private Vertex<KV,VV> value;

    public VertexEvent(EventType type,Vertex<KV,VV> value){
        this.type = type;
        this.value = value;
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public Vertex<KV, VV> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "VertexEvent{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
