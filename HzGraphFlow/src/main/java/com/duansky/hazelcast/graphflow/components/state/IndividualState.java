package com.duansky.hazelcast.graphflow.components.state;

import com.duansky.hazelcast.graphflow.components.Event;
import com.duansky.hazelcast.graphflow.components.State;

/**
 * Individual State is the state that is made up by single individual factor,
 * in other words, its key-value pairs. The most common case is that the state
 * suggests every single vertex state.
 *
 * @param <KV> the type of the vertex id.
 * @param <SV> the type of the vertex value.
 *
 * Created by SkyDream on 2017/2/15.
 */
public interface IndividualState<KV,SV,E extends Event> extends State<E> {
    /**
     * get the current state of the specific vertex.
     * @param id the id of the vertex.
     * @return the current state of the specific vertex.
     */
    SV get(KV id);

    /**
     * update the specific vertex state.
     * @param id the id of the vertex.
     * @param value the new value of this vertex.
     */
    void set(KV id, SV value);
}
