package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.AbstractAlgorithm;
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
public class DegreeDistribution<KV,EV> extends AbstractAlgorithm<EdgeAddEventStreamFromFile<KV,EV>,DegreeDistributionState<KV,EV>>{

    /** data **/
    private String path;

    /** storage **/
    private static final HazelcastInstance STORAGE = Hazelcast.newHazelcastInstance();

    public DegreeDistribution(String path,Class<KV> kvClass,Class<EV> evClass){
        super(new EdgeAddEventStreamFromFile<KV, EV>(path,kvClass,evClass),new DegreeDistributionState<KV,EV>(STORAGE));
        this.path = path;
    }

    public void run(){
        while(stream.hasNext()){
            state.update(stream.next());
            System.out.println("==============");
            Map<KV,Long> degrees = state.getCurrentState();
            for(Map.Entry entry : degrees.entrySet())
                System.out.println(entry.getKey() + ":" + entry.getValue());
        }

    }

    public static void main(String args[]){
        String path = EdgeAddEventStreamFromFile.class.getClassLoader().getResource("").getPath() +"graph.txt";
        DegreeDistribution<Integer,Integer> distribution = new DegreeDistribution(path,Integer.class,Integer.class);
        distribution.run();
    }

}
