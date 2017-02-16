package com.duansky.hazelcast.graphflow.components.state;

import com.duansky.hazelcast.graphflow.util.Constracts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.TreeSet;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class NeighborState<KV> implements IndividualState<KV,TreeSet<KV>> {

    private HazelcastInstance hzi;
    private IMap<KV,TreeSet<KV>> neighbors;

    public NeighborState(HazelcastInstance hzi){
        this.hzi = hzi;
        this.neighbors = hzi.getMap(Constracts.NEIGHBORHOOD_STATE);
    }

    public NeighborState() {
    }

    public TreeSet<KV> get(KV id) {
        return neighbors.get(id);
    }

    public boolean containsKey(KV id){
        return neighbors.containsKey(id);
    }

    public void lockKey(KV id){
        neighbors.lock(id);
    }

    public void unlockKey(KV id){
        neighbors.unlock(id);
    }

    public void addNeighbor(KV source, KV target){
        if(neighbors.containsKey(source)){
            neighbors.lock(source);
            TreeSet<KV> set = neighbors.get(source);
            set.add(target);
            update(source,set);
            neighbors.unlock(source);
        }else{
            TreeSet<KV> set = new TreeSet<KV>();
            set.add(target);
            update(source,set);
        }

    }

    public void update(KV id, TreeSet<KV> value) {
        neighbors.set(id,value);
    }

    public HazelcastInstance getHzi() {
        return hzi;
    }
}
