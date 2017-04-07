package com.duansky.hazelcast.graphflow.components.state;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Contracts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.EntryProcessor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * OutNeighborWithEdgeValueState store and provide access to the neighbors and its edge values of the specific vertex.
 * Here we suppose the edge is directed namely the neighbors of the vertex is out.
 * Which means for the edge(source,target,value), the vertex target is the neighbor
 * of the source vertex, but the source vertex is not the neighbor of the target.
 *
 * directed
 *
 * Created by SkyDream on 2017/2/16.
 */
public class OutNeighborWithEdgeValueState<KV,EV> extends AbstractIndividualState<KV,Set<Edge<KV, EV>>,EdgeEvent<KV,EV>> implements IndividualState<KV,Set<Edge<KV,EV>>,EdgeEvent<KV,EV>>{

    public OutNeighborWithEdgeValueState(String name,HazelcastInstance hi){
        super(Contracts.OUT_NEIGHBORHOOD_WITH_EDGE_VALUE_STATE+"-"+name,hi);
    }

    public void set(KV id, Set<Edge<KV, EV>> value) {
        state.set(id,value);
    }

    public boolean containsKey(KV id){
        return state.containsKey(id);
    }

    public void lockKey(KV id){
        state.lock(id);
    }

    public void unlockKey(KV id){
        state.unlock(id);
    }

    public boolean addNeighbor(Edge<KV,EV> edge){
        KV source = edge.getSource();
        try{
            if(state.isLocked(source))
                UPDATE_CONFLICT_COUNTER.incrementAndGet();
            state.lock(source);
            if(state.containsKey(source)){
                Set<Edge<KV,EV>> edges = state.get(source);
                edges.add(edge);
                set(source,edges);
            }else{
                Set<Edge<KV,EV>> edges = new HashSet<Edge<KV, EV>>();
                edges.add(edge);
                set(source,edges);
            }
            return true;
        }finally {
            state.unlock(source);
        }

    }


    public boolean update(EdgeEvent<KV, EV> event) {
        EventType type = event.getType();
        Edge<KV,EV> edge = event.getValue();
        switch(type){
            case ADD:
                return addNeighbor(edge);
            default:
                throw new UnsupportedOperationException("the update for event delete and update is not supported by now.");
        }
    }
}
