package com.duansky.hazelcast.graphflow.components.state;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Contracts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ISet;

/**
 * Created by SkyDream on 2017/3/20.
 */
public class VertexNumberState<KV,EV> implements IntegralState<Long,EdgeEvent<KV,EV>> {

    public static String DEFAULT_NAME = Contracts.VERTEX_NUMBER_STATE;
    protected ISet<KV> verities;
    protected HazelcastInstance hi;
    protected String name;

    public VertexNumberState(HazelcastInstance hi){
        this(DEFAULT_NAME,hi);
    }

    public VertexNumberState(String name,HazelcastInstance hi){
        this.name = name;
        this.hi = hi;
        this.verities = hi.getSet(name);
    }

    @Override
    public Long get() {
        return (long)verities.size();
    }

    @Override
    public boolean update(EdgeEvent<KV, EV> event) {
        Edge<KV,EV> edge = event.getValue();
        verities.add(edge.getSource());
        verities.add(edge.getTarget());
        return true;
    }
}
