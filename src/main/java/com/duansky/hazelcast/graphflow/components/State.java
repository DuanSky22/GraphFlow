package com.duansky.hazelcast.graphflow.components;

import com.duansky.hazelcast.graphflow.components.state.IndividualState;
import com.duansky.hazelcast.graphflow.components.state.IntegralState;

/**
 * State is the current outlook of the specific graph algorithm on the specific event stream.
 * Here we define two state:{@link IntegralState} and
 * {@link IndividualState}.
 *
 * @param <T> the event type
 * @param <V> the event value type
 *
 * Created by SkyDream on 2017/2/15.
 */
public interface State<E extends Event> {

    /**
     * update the current state.
     */
    boolean update(E event);
}
