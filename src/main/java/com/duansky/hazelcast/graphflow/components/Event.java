package com.duansky.hazelcast.graphflow.components;

/**
 * Created by SkyDream on 2017/2/15.
 * Event defines how graph changes. It contains event type({@link com.duansky.hazelcast.graphflow.components.event.EventType})
 * and event value. Event type defines the action(add,delete,update) of this event, while event value
 * defines the actual data(edge or vertex value) of the event.
 *
 * @param <T> the event type
 * @param <V> the event value type
 */
public interface Event<T,V> {

    /**
     * get the type of this event.
     * @return the type of this event.
     */
    T getType();

    /**
     * get the value of this event.
     * @return the value of this event.
     */
    V getValue();

}
