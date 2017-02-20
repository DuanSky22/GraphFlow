package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.AbstractIndividualState;
import com.duansky.hazelcast.graphflow.components.state.OutNeighborState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.util.Contracts;
import com.hazelcast.core.HazelcastInstance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by SkyDream on 2017/2/17.
 */
public class PageRankState<KV,EV> extends AbstractIndividualState<KV,Double,EdgeEvent<KV,EV>> {

    /** algorithm parameters **/
    private double delta = 0.2;
    private int maxIteration = 10000;

    /** tools **/
    private OutNeighborState<KV,EV> neighborState;

    public PageRankState(HazelcastInstance hi){
        super(Contracts.PAGERANK_STATE,hi);
        neighborState = new OutNeighborState<KV, EV>(hi);
    }

    public PageRankState(HazelcastInstance hi, double delta,int maxIteration){
        this(hi);
        this.delta = delta;
        this.maxIteration = maxIteration;
    }

    protected Double init(){
        return 1.0d;
    }
    protected Double calculate(boolean useDelta,Double original,Double increased){
        if(useDelta) return delta * increased + (1-delta) * original;
        else return increased + original;
    }

    public boolean update(EdgeEvent<KV, EV> event) {
        EventType type = event.getType();
        Edge<KV,EV> edge = event.getValue();
        KV source = edge.getSource(), target = edge.getTarget();
        switch (type){
            case ADD:
                neighborState.update(event);
                //if the verities of the edge is new, we just init it.
                if(!state.containsKey(source) && !state.containsKey(target)){
                    state.set(source,init());
                    state.set(target,init());
                    spread(source);
                }else{
                   spread(source);
                }
                return true;
            default:
                throw new UnsupportedOperationException("The delete and update type events are not supported by now.");
        }
    }

    private void spread(KV source) {
        if(!state.containsKey(source)) state.put(source,init());
        // set the current state as the init state.
        Map<KV,Double> original = getCurrentState();
        Map<KV,Double[]> cached = new HashMap<>();
        Set<KV> actives = new HashSet<>();

        for(Map.Entry<KV,Double> pair : original.entrySet()){
            Double[] ot = new Double[2]; // ot:(original,increased)
            ot[0] = pair.getValue();
            ot[1] = 0d;
            cached.put(pair.getKey(),ot);
        }

        actives.add(source); int step = 1;
        while(!actives.isEmpty() && step <= maxIteration){
            Set<KV> newActives = new HashSet<>();
            for(KV active : actives){
                Double v = calculate(true,cached.get(active)[0],cached.get(active)[1]);
                // send the page rank value to its neighbors.
                Set<KV> neighbors = neighborState.get(active); // get all its out neighbors.
                if(neighbors != null && !neighbors.isEmpty()){
                    int size = neighbors.size();
                    v = v / size;
                    for(KV neighbor : neighbors){
                        Double[] old = cached.get(neighbor);
                        if(old == null){
                            old = new Double[2];
                            old[0] = 1d; old[1] = v;
                            cached.put(neighbor,old);
                        }
                        else
                            old[1] += v; //increase the page rank value.
                        newActives.add(neighbor);
                    }
                    //reset itself page rank value to zero.
                    Double[] refresh = cached.get(active);
                    refresh[0] = 0d;
                }
            }
            actives = newActives;
            step++;
        }
        Double newValue;
        for(Map.Entry<KV,Double[]> entry : cached.entrySet()){
            newValue = calculate(true,entry.getValue()[0],entry.getValue()[1]);
            set(entry.getKey(),newValue);
        }
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public int getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }
}
