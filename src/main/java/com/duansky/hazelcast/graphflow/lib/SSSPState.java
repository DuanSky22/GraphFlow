package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.IndividualState;
import com.duansky.hazelcast.graphflow.components.state.NeighborEdgeValueState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Contracts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.Map;
import java.util.Set;

/**
 * SSSP: Single Source Shortest Path.
 *
 * directed
 *
 * Created by SkyDream on 2017/2/15.
 */
public class SSSPState<KV,EV extends Number> implements IndividualState<KV, Long,EdgeEvent<KV, EV>>{

    /**the start vertex of the SSSP **/
    private KV original;

    /**the tools**/
    private NeighborEdgeValueState<KV, EV> neighborState;

    /**the final result of SSSP, only reachable vertex can be added to this map. **/
    private IMap<KV, Long> state;

    HazelcastInstance hi;


    public SSSPState(HazelcastInstance hi,KV original){
        this.hi = hi;
        this.original = original;
        this.neighborState = new NeighborEdgeValueState<KV, EV>(hi);
        this.state = hi.getMap(Contracts.SSSP_STATE);
        set(original,0L); // the original vertex is the seed.
    }

    public Long get(KV id) {
        if(state.containsKey(id)) return state.get(id);
        else return -1L;
    }

    public void set(KV id, Long value) {
        state.set(id,value);
    }

    //TODO here we need think more.
    public void spread(KV id,Long value){
        //if the vertex is not already in state and its closer to original vertex, we will change nothing.
        if(state.containsKey(id) && state.get(id) <= value)
            return;
        set(id,value);
        Set<Edge<KV, EV>> neighbors = neighborState.get(id);
        KV target; Long tarOldValue,tarNewValue;
        if(neighbors == null) return;
        for(Edge<KV,EV> edge : neighbors){
            target = edge.getTarget();
            if(state.containsKey(target)){//if this vertex has already calculated.
                tarOldValue = get(target).longValue();
                tarNewValue = value + edge.getEdgeValue().longValue();
                if( tarNewValue < tarOldValue){ // if the new value is smaller.
                    spread(target,tarNewValue);
                }
            }else{//else the vertex is reachable now.
                tarNewValue = value + edge.getEdgeValue().longValue();
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
                    Long newValue = get(source) + edge.getEdgeValue().longValue();
                    spread(target,newValue);
                }
                return true;
            default:
                throw new UnsupportedOperationException("The delete and update type events are not supported by now.");
        }
    }

    public Map<KV,Long> getCurrentState(){
        return hi.getMap(Contracts.SSSP_STATE);
    }
}
