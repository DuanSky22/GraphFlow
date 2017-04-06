package com.duansky.hazelcast.graphflow.components.state;

import com.duansky.hazelcast.graphflow.components.Event;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by SkyDream on 2017/2/17.
 */
public abstract class AbstractIndividualState<KV,SV,E extends Event> implements IndividualState<KV,SV,E>{

    protected IMap<KV,SV> state;
    protected String name;
    protected HazelcastInstance hi;
    public static AtomicLong UPDATE_CONFLICT_COUNTER = new AtomicLong(0);

    public AbstractIndividualState(String name,HazelcastInstance hi){
        this.name = name;
        this.hi = hi;
        this.state = hi.getMap(name);
    }

    public SV get(KV id) {
        return state.get(id);
    }

    public void set(KV id, SV value) {
        state.put(id,value);
    }

    public boolean containsKey(KV id){
        return state.containsKey(id);
    }

    public abstract boolean update(E event);

    public String getName() {
        return name;
    }

    public HazelcastInstance getHi() {
        return hi;
    }

    public Map<KV,SV> getCurrentState(){
        return hi.getMap(name);
    }
}
