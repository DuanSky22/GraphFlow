package com.duansky.hazelcast.graphflow.components.state;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Contracts;
import com.hazelcast.core.HazelcastInstance;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by SkyDream on 2017/2/17.
 */
public class InOutNeighborState<KV,EV> extends AbstractIndividualState<KV,Set<KV>[],EdgeEvent<KV,EV>> {


    public InOutNeighborState(HazelcastInstance hi) {
        super(Contracts.IN_OUT_NEIGHBORHOOD_STATE+System.currentTimeMillis(), hi);
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

    protected boolean addNeighbor(KV source,KV target){
        Set<KV>[] sourceInout,targetInout;
        state.lock(source);state.lock(target);
        if(state.containsKey(source)){
            sourceInout = state.get(source);
            sourceInout[1].add(target);
        }else{
            sourceInout = new HashSet[2];
            sourceInout[0] = new HashSet<KV>();
            sourceInout[1] = new HashSet<KV>();
            sourceInout[1].add(target);
        }

        if(state.containsKey(target)){
            targetInout = state.get(target);
            targetInout[0].add(source);
        }else{
            targetInout = new HashSet[2];
            targetInout[0] = new HashSet<KV>();
            targetInout[1] = new HashSet<KV>();
            targetInout[0].add(source);
        }
        state.put(source,sourceInout);
        state.put(target,targetInout);
        state.unlock(source);state.unlock(target);
        return true;
    }

    protected boolean deleteNeighbor(KV source,KV target){
        boolean res = true;
        Set<KV>[] inout;
        state.lock(source);state.lock(target);
        if(state.containsKey(source) && ((inout = state.get(source)) != null)){
            res &= inout[1].remove(target);
        }
        if(state.containsKey(target) && ((inout = state.get(target)) != null)){
            res &= inout[0].remove(source);
        }
        state.unlock(source);state.unlock(target);
        return res;
    }
}
