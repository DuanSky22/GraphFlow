package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.state.IndividualState;
import com.duansky.hazelcast.graphflow.util.Constracts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.Map;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class DegreeDistributionState<KV> implements IndividualState<KV,Long> {

    IMap<KV,Long> data;
    HazelcastInstance hi;

    public DegreeDistributionState(HazelcastInstance hi){
        this.hi = hi;
        data = hi.getMap(Constracts.DEGREE_DISTRIBUTION_STATE);
    }

    public Long get(KV id) {
        return data.get(id);
    }

    public boolean increase(KV id){
        if(data.containsKey(id)){
            data.lock(id);
            data.put(id,data.get(id)+1);
            data.unlock(id);
        }else
            data.put(id,1L);
        return true;
    }

    public boolean decrease(KV id){
        if(data.containsKey(id)){
            data.lock(id);
            long count = data.get(id);
            if(count > 0) data.put(id,count-1);
            else return false;
            data.unlock(id);
        }
        return false;
    }

    public Map<KV,Long> getCurrentState(){
        return hi.getMap(Constracts.DEGREE_DISTRIBUTION_STATE);
    }

    public void update(KV id, Long value) {
        throw new UnsupportedOperationException();
    }
}
