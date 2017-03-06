package com.duansky.hazelcast.graphflow.components;

/**
 * EventStream is the stream of the {@link Event}.
 *
 * @param <T> the event type
 * @param <V> the event value type.
 *
 * Created by SkyDream on 2017/2/15.
 */
public interface EventStream<T,V> {
    /**
     * If this event stream ended.
     * @return true if this event is ended, otherwise false.
     */
    boolean hasNext();

    /**
     * get the next event of this event stream contains.
     * @return the next event of this event stream contains.
     */
    Event<T,V> next();
}
