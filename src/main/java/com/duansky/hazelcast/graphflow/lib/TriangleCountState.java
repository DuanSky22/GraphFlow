package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.Event;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.IntegralState;
import com.duansky.hazelcast.graphflow.components.state.NeighborState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Constracts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;

import java.util.TreeSet;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class TriangleCountState<KV,EV> implements IntegralState<Long,EventType,Edge<KV,EV>>{

    IAtomicLong counter;
    HazelcastInstance hi;
    NeighborState<KV> neighborState;

    public TriangleCountState(HazelcastInstance hi){
        this.hi = hi;
        this.counter = hi.getAtomicLong(Constracts.TRIANGLE_COUNT_STATE);
        neighborState = new NeighborState<KV>(hi);
    }

    public void update(Event<EventType, Edge<KV, EV>> event) {
        EventType type = event.getType();
        Edge<KV,EV> edge = event.getValue();
        switch(type){
            case ADD:
                KV source = edge.getSource();
                KV target = edge .getTarget();

                neighborState.addNeighbor(source,target);
                neighborState.addNeighbor(target,source);

                neighborState.lockKey(source); neighborState.lockKey(target);
                TreeSet<KV> sn = neighborState.get(source);
                TreeSet<KV> tn = neighborState.get(target);

                int increased = 0;
                if(sn.size() < tn.size()){
                    for(KV vertex : sn)
                        if(tn.contains(vertex)) increased++;
                }else{
                    for(KV vertex : tn)
                        if(sn.contains(vertex)) increased++;
                }
                counter.addAndGet(increased);
                neighborState.unlockKey(source); neighborState.unlockKey(target);
                break;
            default:
                break;
        }
    }

    public Long get() {
        return counter.get();
    }

}
