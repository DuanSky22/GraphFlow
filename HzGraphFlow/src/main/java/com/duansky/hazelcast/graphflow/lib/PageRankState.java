package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.AbstractIndividualState;
import com.duansky.hazelcast.graphflow.components.state.InOutNeighborState;
import com.duansky.hazelcast.graphflow.components.state.OutNeighborState;
import com.duansky.hazelcast.graphflow.components.state.VertexNumberState;
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
    private int maxIteration = 10;
    private double threshold = 0.0001;

    /** tools **/
    private InOutNeighborState<KV,EV> neighborState;
    private VertexNumberState<KV,EV> vertexNumberState;

    public PageRankState(String name,HazelcastInstance hi){
        super(Contracts.PAGERANK_STATE+"-"+name,hi);
        neighborState = new InOutNeighborState<KV, EV>(Contracts.PAGERANK_STATE+"-"+name,hi);
        vertexNumberState = new VertexNumberState<KV, EV>(hi);
    }

    public PageRankState(String name,HazelcastInstance hi, double delta,int maxIteration){
        this(name,hi);
        this.delta = delta;
        this.maxIteration = maxIteration;
    }

    protected Double init(){
        long vertexNumber = vertexNumberState.get();
        return (1-delta)/vertexNumber;
    }

    protected Double calculate(boolean useDelta,Double original,Double increased,int N){
        if(useDelta) return delta * increased + (1-delta) / N;
        else return increased + original;
    }

    public boolean update(EdgeEvent<KV, EV> event) {
        EventType type = event.getType();
        Edge<KV,EV> edge = event.getValue();
        KV source = edge.getSource(), target = edge.getTarget();
        switch (type){
            case ADD:
                neighborState.update(event);
                vertexNumberState.update(event);
                //if the verities of the edge is new, we just init it.
                state.lock(source);
                if(!state.containsKey(source)) state.set(source, init());
                state.unlock(source);
                state.lock(target);
                if(!state.containsKey(target)) state.set(target, init());
                state.unlock(target);
                spread(source,1);
                return true;
            default:
                throw new UnsupportedOperationException("The delete and update type events are not supported by now.");
        }
    }

    private void spread(KV source,int step) {
        if(step > maxIteration) return;
        long vertexNum = vertexNumberState.get();
        if(neighborState.isLocked(source) || state.isLocked(source))
            UPDATE_CONFLICT_COUNTER.incrementAndGet();

        neighborState.lock(source); state.lock(source);

        Set<KV> sets[] = neighborState.get(source);
        double prOld = state.get(source);
        if(sets != null){
            Set<KV> in = sets[0], out = sets[1];
            //re calculate the pr value.
            double pr = (1-delta)/vertexNum;
            double sum = 0;
            if(in != null && !in.isEmpty()){
                for(KV vertex : in){
                    sum += delta * getAccumulate(vertex);
                }
            }
            pr += sum;
            if(Math.abs(pr - prOld) < threshold) return;
            set(source,pr);

            state.unlock(source);neighborState.unlock(source);

            if(out != null && !out.isEmpty()){
                for(KV vertex : out){
                    spread(vertex,step+1);
                }
            }
        }else{
            state.unlock(source);neighborState.unlock(source);
        }
    }

    private double getAccumulate(KV vertex){
        if(state.containsKey(vertex)){
            double pr = state.get(vertex);
            Set<KV> sets[] = neighborState.get(vertex);
            int size = (sets != null && sets[1] != null) ? sets[1].size() : 0;
            if(size != 0)
                return pr / size;
        }
        return 0d;
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
