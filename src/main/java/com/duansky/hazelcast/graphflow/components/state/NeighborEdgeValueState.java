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
 * NeighborEdgeValueState store and provide access to the neighbors and its edge values of the specific vertex.
 * Here we suppose the edge is directed namely the neighbors of the vertex is out.
 * Which means for the edge(source,target,value), the vertex target is the neighbor
 * of the source vertex, but the source vertex is not the neighbor of the target.
 *
 * directed
 *
 * Created by SkyDream on 2017/2/16.
 */
public class NeighborEdgeValueState<KV,EV> implements IndividualState<KV,Set<Edge<KV,EV>>,EdgeEvent<KV,EV>>{

    private HazelcastInstance hzi;
    private IMap<KV,Set<Edge<KV,EV>>> neighbors;

    public NeighborEdgeValueState(HazelcastInstance hzi){
        this.hzi = hzi;
        this.neighbors = hzi.getMap(Contracts.NEIGHBORHOOD_WITH_EDGE_VALUE_STATE);
    }

    public Set<Edge<KV, EV>> get(KV id) {
        return neighbors.get(id);
    }

    public void set(KV id, Set<Edge<KV, EV>> value) {
        neighbors.set(id,value);
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

    public boolean addNeighbor(Edge<KV,EV> edge){
        KV source = edge.getSource();
        if(neighbors.containsKey(source)){
            neighbors.lock(source);
            Set<Edge<KV,EV>> edges = neighbors.get(source);
            edges.add(edge);
            set(source,edges);
            neighbors.unlock(source);
        }else{
            Set<Edge<KV,EV>> edges = new HashSet<Edge<KV, EV>>();
            edges.add(edge);
            set(source,edges);
        }
        return true;
    }

    public HazelcastInstance getHzi(){
        return hzi;
    }

    public boolean update(EdgeEvent<KV, EV> event) {
        EventType type = event.getType();
        Edge<KV,EV> edge = event.getValue();
        switch(type){
            case ADD:
                return addNeighbor(edge);
            default:
                throw new UnsupportedOperationException("the update for event delete and update is not supported by now.");
        }
    }
}
