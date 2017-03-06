package com.duansky.hazelcast.graphflow.components.event;

import com.duansky.hazelcast.graphflow.components.Event;
import com.duansky.hazelcast.graphflow.graph.Edge;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class EdgeEvent<KV,EV> implements Event<EventType,Edge<KV,EV>>{

    private EventType type;
    private Edge<KV, EV> value;

    public EdgeEvent(EventType type,Edge<KV, EV> value){
        this.type = type;
        this.value = value;
    }


    public EventType getType() {
        return type;
    }

    public Edge<KV, EV> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "EdgeEvent{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
