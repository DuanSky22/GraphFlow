package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.IndividualState;
import com.duansky.hazelcast.graphflow.components.state.NeighborEdgeValueState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.hazelcast.core.IMap;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class SSSPState<KV> implements IndividualState<KV, Number,EdgeEvent<KV, Number>>{

    /**the start vertex of the SSSP **/
    private KV original;

    /**the tools**/
    private NeighborEdgeValueState<KV, Number> neighbors;

    /**the final result of SSSP, only reachable vertex can be added to this map. **/
    private IMap<KV, Number> state;

    public Number get(KV id) {
        if(state.containsKey(id)) return state.get(id);
        else return null;
    }

    public void set(KV id, Number value) {
        state.set(id,value);
    }


    public boolean update(EdgeEvent<KV, Number> event) {
        EventType type = event.getType();
        Edge<KV, Number> edge = event.getValue();
        switch (type){
            case ADD:
                if(state.containsKey(edge.getSource()) && state.containsKey(edge.getTarget()))
                    updateWithin(edge);

        }
        neighbors.update(event);

        return true;
    }

    private void updateWithin(Edge<KV, Number> edge) {
        Long number1 = state.get(edge.getSource()).longValue();
        Long number2 = state.get(edge.getTarget()).longValue();
        Long distance = edge.getEdgeValue().longValue();
        Long diff = number1-number2;
        if(distance < diff){// if the added edge makes this two verities more closer.

        }
    }
}
