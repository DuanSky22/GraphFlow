package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.AbstractIndividualState;
import com.duansky.hazelcast.graphflow.components.state.IndividualState;
import com.duansky.hazelcast.graphflow.components.state.OutNeighborWithEdgeValueState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Contracts;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;
import java.util.Set;

/**
 * SSSP: Single Source Shortest Path.
 *
 * directed
 *
 * Created by SkyDream on 2017/2/15.
 */
public class SSSPState<KV,EV extends Number> extends AbstractIndividualState<KV, Double,EdgeEvent<KV, EV>> implements IndividualState<KV, Double,EdgeEvent<KV, EV>>{

    /**the start vertex of the SSSP **/
    private KV original;

    /**the tools**/
    private OutNeighborWithEdgeValueState<KV, EV> neighborState;

    public SSSPState(String name,HazelcastInstance hi,KV original){
        super(Contracts.SSSP_STATE+"-"+name,hi);
        this.original = original;
        this.neighborState = new OutNeighborWithEdgeValueState<KV, EV>(Contracts.SSSP_STATE+"-"+name,hi);
        set(original,0d); // the original vertex is the seed.
    }

    public Double get(KV id) {
        if(state.containsKey(id)) return state.get(id);
        else return Double.MAX_VALUE;
    }

    //TODO here we need think more.
    public void spread(KV id,Double value){
        //if the vertex is not already in state and its closer to original vertex, we will change nothing.
        if(state.isLocked(id))
            UPDATE_CONFLICT_COUNTER.incrementAndGet();
        state.lock(id);
        if(state.containsKey(id) && state.get(id) <= value){
            state.unlock(id);
            return;
        }
        set(id,value);
        state.unlock(id);
        Set<Edge<KV, EV>> neighbors = neighborState.get(id);
        KV target; Double tarOldValue,tarNewValue;
        if(neighbors == null) return;
        for(Edge<KV,EV> edge : neighbors){
            target = edge.getTarget();
            if(state.containsKey(target)){//if this vertex has already calculated.
                tarOldValue = get(target).doubleValue();
                tarNewValue = value + edge.getEdgeValue().doubleValue();
                if( tarNewValue < tarOldValue){ // if the new value is smaller.
                    spread(target,tarNewValue);
                }
            }else{//else the vertex is reachable now.
                tarNewValue = value + edge.getEdgeValue().doubleValue();
                spread(target,tarNewValue);
            }
        }
    }

    public boolean update(EdgeEvent<KV, EV> event) {
        EventType type = event.getType();
        Edge<KV, EV> edge = event.getValue();
        KV source = edge.getSource(), target = edge.getTarget();
        switch (type){
            case ADD:
                neighborState.update(event); //update the neighbors.
                if(state.containsKey(source)){
                    Double newValue = get(source) + edge.getEdgeValue().doubleValue();
                    spread(target,newValue);
                }
                return true;
            default:
                throw new UnsupportedOperationException("The delete and update type events are not supported by now.");
        }
    }
}
