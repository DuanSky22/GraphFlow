package com.duansky.hazelcast.graphflow.components.state;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Contracts;
import com.hazelcast.core.HazelcastInstance;

import java.util.HashSet;
import java.util.Set;

/**
 * Neighbor state store and provide access to the neighbors of the specific vertex.
 * Here we suppose the edge is directed namely the neighbors of the vertex is out.
 * Which means for the edge(source,target,value), the vertex target is the neighbor
 * of the source vertex, but the source vertex is not the neighbor of the target.
 *
 * directed
 *
 * Created by SkyDream on 2017/2/15.
 */
public class OutNeighborState<KV,EV> extends AbstractIndividualState<KV,Set<KV>,EdgeEvent<KV,EV>> implements IndividualState<KV,Set<KV>,EdgeEvent<KV,EV>> {


    public OutNeighborState(HazelcastInstance hi){
        super(Contracts.OUT_NEIGHBORHOOD_STATE+System.currentTimeMillis(),hi);
    }

    public void lockKey(KV id){
        state.lock(id);
    }

    public void unlockKey(KV id){
        state.unlock(id);
    }

    protected boolean addNeighbor(KV source, KV target){
        if(state.containsKey(source)){
            state.lock(source);
            Set<KV> set = state.get(source);
            set.add(target);
            set(source,set);
            state.unlock(source);
        }else{
            Set<KV> set = new HashSet<KV>();
            set.add(target);
            set(source,set);
        }
        return true;
    }

    protected boolean deleteNeighbor(KV source, KV target){
        if(state.containsKey(source)){
            state.lock(source);
            Set<KV> set = state.get(source);
            set.remove(target);
            set(source,set);
            state.unlock(source);
            return true;
        }
        return false;
    }

    public boolean update(EdgeEvent<KV, EV> event) {
        EventType type = event.getType();
        Edge<KV,EV> edge = event.getValue();
        switch(type){
            case ADD:
                return addNeighbor(edge.getSource(),edge.getTarget());
            case DELETE:
                return deleteNeighbor(edge.getSource(),edge.getTarget());
            default:
                return false;
        }
    }
}
