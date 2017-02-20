package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.AbstractAlgorithm;
import com.duansky.hazelcast.graphflow.components.event.EdgeAddEventStreamFromFile;
import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.IndividualState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class TriangleCount<KV,EV,VV> extends AbstractAlgorithm<EdgeAddEventStreamFromFile<KV,EV>,TriangleCountState<KV,EV>>{
    /** data **/
    private String path;

    /** storage **/
    private static final HazelcastInstance STORAGE = Hazelcast.newHazelcastInstance();

    public TriangleCount(String path,Class<KV> kvClass,Class<EV> evClass){
        super(new EdgeAddEventStreamFromFile<KV, EV>(path,kvClass,evClass),new TriangleCountState<KV,EV>(STORAGE));
        this.path = path;
    }

    public void run(){
        while(stream.hasNext()) {
            System.out.println("==============");
            EdgeEvent<KV, EV> edgeEvent = stream.next();
            state.update(edgeEvent);
            System.out.println(state.get());
        }
    }
    public static void main(String args[]){
        String path = EdgeAddEventStreamFromFile.class.getClassLoader().getResource("").getPath() +"graph-10000-0.2-edges.txt";
        TriangleCount<Integer,Integer,Integer> triangleCount = new TriangleCount(path,Integer.class,Integer.class);
        triangleCount.run();
    }
}
