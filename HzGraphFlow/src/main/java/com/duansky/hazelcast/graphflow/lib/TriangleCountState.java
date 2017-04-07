package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.IntegralState;
import com.duansky.hazelcast.graphflow.components.state.OutNeighborState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Contracts;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;

import java.util.Set;

/**
 * Triangle Count count the total different triangle of the specific graph.
 * Here we suppose the graph is undirected.
 *
 * undirected
 *
 * Created by SkyDream on 2017/2/15.
 */
public class TriangleCountState<KV,EV> implements IntegralState<Long,EdgeEvent<KV,EV>>{

    IAtomicLong counter;
    HazelcastInstance hi;
    OutNeighborState<KV,EV> outNeighborState;

    /**detect the conflict**/
    public static long LOCK_CONFLICT = 0;

    public TriangleCountState(String name,HazelcastInstance hi){
        this.hi = hi;
        this.counter = hi.getAtomicLong(Contracts.TRIANGLE_COUNT_STATE+"-"+name);
        outNeighborState = new OutNeighborState<KV,EV>(Contracts.TRIANGLE_COUNT_STATE+"-"+name,hi);
    }

    public boolean update(EdgeEvent<KV,EV> event) {
        EventType type = event.getType();
        Edge<KV,EV> edge = event.getValue();
        switch(type){
            case ADD:
                KV source = edge.getSource();
                KV target = edge .getTarget();

                //TODO dead lock?
                if(outNeighborState.isLocked(source) || outNeighborState.isLocked(target))
                    LOCK_CONFLICT++;

                outNeighborState.lockKey(source); outNeighborState.lockKey(target);

                outNeighborState.update(event);
                outNeighborState.update(new EdgeEvent<KV, EV>(type,edge.reverse()));

                Set<KV> sn = outNeighborState.get(source);
                Set<KV> tn = outNeighborState.get(target);

                int increased = 0;
                if(sn.size() < tn.size()){
                    for(KV vertex : sn)
                        if(tn.contains(vertex)) increased++;
                }else{
                    for(KV vertex : tn)
                        if(sn.contains(vertex)) increased++;
                }
                counter.addAndGet(increased);

                outNeighborState.unlockKey(target); outNeighborState.unlockKey(source);
                return true;
            default:
                return false;
        }
    }

    public Long get() {
        return counter.get();
    }
}
