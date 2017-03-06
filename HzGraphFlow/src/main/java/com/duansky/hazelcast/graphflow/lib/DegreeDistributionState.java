package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.AbstractIndividualState;
import com.duansky.hazelcast.graphflow.components.state.IndividualState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Contracts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.Map;
import java.util.Set;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class DegreeDistributionState<KV,EV> extends AbstractIndividualState<KV,Long,EdgeEvent<KV,EV>> implements IndividualState<KV,Long,EdgeEvent<KV,EV>> {

    public DegreeDistributionState(HazelcastInstance hi){
        super(Contracts.DEGREE_DISTRIBUTION_STATE,hi);
    }

    public boolean increase(KV id){
        if(state.containsKey(id)){
            state.lock(id);
            set(id,state.get(id)+1);
            state.unlock(id);
        }else
            set(id,1L);
        return true;
    }

    public boolean decrease(KV id){
        if(state.containsKey(id)){
            state.lock(id);
            long count = state.get(id);
            if(count > 0) set(id,count-1);
            else return false;
            state.unlock(id);
        }
        return false;
    }

    public boolean update(EdgeEvent<KV, EV> event) {
        EventType type = event.getType();
        Edge<KV,EV> edge = event.getValue();
        switch(type){
            case ADD:
                return increase(edge.getSource()) && increase(edge.getTarget());
            case DELETE:
                return decrease(edge.getSource()) && decrease(edge.getTarget());
            default:
                return false;
        }
    }
}
