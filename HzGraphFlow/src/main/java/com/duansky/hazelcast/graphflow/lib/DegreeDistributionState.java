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

    public DegreeDistributionState(String name,HazelcastInstance hi){
        super(Contracts.DEGREE_DISTRIBUTION_STATE+"-"+name,hi);
    }

    public boolean increase(KV id){
        //use cas to reset value.
        if(state.isLocked(id))
            UPDATE_CONFLICT_COUNTER.incrementAndGet();
        state.lock(id);
        if(!state.containsKey(id)){
            set(id,1l);
            state.unlock(id);
        }else{
            set(id,get(id)+1);
            state.unlock(id);
        }
        return true;
    }

    public boolean decrease(KV id){
        //use cas to reset value.
        for(;;){
            Long oldValue = get(id);
            if(oldValue == null){throw new IllegalArgumentException("the degree of "+id+" vertex has already be zero!");}
            else{
                Long newValue = oldValue - 1;
                if(state.replace(id,oldValue,newValue))
                    break;
            }
        }
//        if(state.containsKey(id)){
//            state.lock(id);
//            long count = state.get(id);
//            if(count > 0) set(id,count-1);
//            else return false;
//            state.unlock(id);
//        }
        return true;
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
