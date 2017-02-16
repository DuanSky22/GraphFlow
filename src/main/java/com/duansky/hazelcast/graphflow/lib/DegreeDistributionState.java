package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.IndividualState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Constracts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.Map;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class DegreeDistributionState<KV,EV> implements IndividualState<KV,Long,EdgeEvent<KV,EV>> {

    IMap<KV,Long> data;
    HazelcastInstance hi;

    public DegreeDistributionState(HazelcastInstance hi){
        this.hi = hi;
        data = hi.getMap(Constracts.DEGREE_DISTRIBUTION_STATE);
    }

    public Long get(KV id) {
        return data.get(id);
    }

    public void set(KV id, Long value) {
        data.put(id,value);
    }

    public boolean increase(KV id){
        if(data.containsKey(id)){
            data.lock(id);
            set(id,data.get(id)+1);
            data.unlock(id);
        }else
            set(id,1L);
        return true;
    }

    public boolean decrease(KV id){
        if(data.containsKey(id)){
            data.lock(id);
            long count = data.get(id);
            if(count > 0) set(id,count-1);
            else return false;
            data.unlock(id);
        }
        return false;
    }

    public Map<KV,Long> getCurrentState(){
        return hi.getMap(Constracts.DEGREE_DISTRIBUTION_STATE);
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
