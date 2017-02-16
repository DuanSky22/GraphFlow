package com.duansky.hazelcast.graphflow.components.state;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Contracts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

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
public class NeighborState<KV,EV> implements IndividualState<KV,Set<KV>,EdgeEvent<KV,EV>> {

    private HazelcastInstance hzi;
    private IMap<KV,Set<KV>> neighbors;

    public NeighborState(HazelcastInstance hzi){
        this.hzi = hzi;
        this.neighbors = hzi.getMap(Contracts.NEIGHBORHOOD_STATE);
    }

    public Set<KV> get(KV id) {
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

    protected boolean addNeighbor(KV source, KV target){
        if(neighbors.containsKey(source)){
            neighbors.lock(source);
            Set<KV> set = neighbors.get(source);
            set.add(target);
            set(source,set);
            neighbors.unlock(source);
        }else{
            Set<KV> set = new HashSet<KV>();
            set.add(target);
            set(source,set);
        }
        return true;
    }

    protected boolean deleteNeighbor(KV source, KV target){
        if(neighbors.containsKey(source)){
            neighbors.lock(source);
            Set<KV> set = neighbors.get(source);
            set.remove(target);
            set(source,set);
            neighbors.unlock(source);
            return true;
        }
        return false;
    }

    public void set(KV id, Set<KV> value) {
        neighbors.set(id,value);
    }

    public HazelcastInstance getHzi() {
        return hzi;
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
