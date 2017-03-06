package com.duansky.hazelcast.graphflow.components.state;

import com.duansky.hazelcast.graphflow.components.Event;
import com.duansky.hazelcast.graphflow.components.State;

/**
 * Integral State is the state that the contains only on factor.
 * The opposite is the individual state which is made up by many individual factors.
 * For example, we use only on counter to suggest the global triangle count state,
 * this kind of state is the integral state. While we use as many as the number of verities
 * to suggest the local triangle count state, this kind of state is the individual state.
 *
 * @param <F> the state type
 *
 * Created by SkyDream on 2017/2/15.
 */
public interface IntegralState<F,E extends Event> extends State<E>{
    /**
     * get the current state.
     * @return the current state.
     */
    F get();

}
