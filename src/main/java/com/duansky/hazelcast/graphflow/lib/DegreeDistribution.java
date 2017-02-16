package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.event.EdgeAddEventStreamFromFile;
import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class DegreeDistribution<KV,EV> {

    /** data **/
    private String path;

    /** components **/
    private EdgeAddEventStreamFromFile<KV,EV> stream;
    private DegreeDistributionState<KV> state;

    /** storage **/
    private static final HazelcastInstance STORAGE = Hazelcast.newHazelcastInstance();

    public DegreeDistribution(String path){
        this.path = path;
        this.stream = new EdgeAddEventStreamFromFile<KV, EV>(path);
        state = new DegreeDistributionState<KV>(STORAGE);
    }

    public void run(){
        while(stream.hasNext()){
            EdgeEvent<KV,EV> edgeEvent = stream.next();
            EventType type = edgeEvent.getType();
            Edge<KV,EV> value = edgeEvent.getValue();

            switch (type){
                case ADD:
                    state.increase(value.getSource());
                    state.increase(value.getTarget());
                    break;
                case DELETE:
                    state.decrease(value.getSource());
                    state.decrease(value.getTarget());
                    break;
                default:
                    break;
            }

            System.out.println("==============");
            Map<KV,Long> degrees = state.getCurrentState();
            for(Map.Entry entry : degrees.entrySet())
                System.out.println(entry.getKey() + ":" + entry.getValue());
        }

    }

    public static void main(String args[]){
        String path = EdgeAddEventStreamFromFile.class.getClassLoader().getResource("").getPath() +"graph.txt";
        DegreeDistribution<Integer,Integer> distribution = new DegreeDistribution(path);
        distribution.run();
    }

}
