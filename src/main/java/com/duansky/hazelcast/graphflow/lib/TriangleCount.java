package com.duansky.hazelcast.graphflow.lib;

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
public class TriangleCount<KV,EV,VV> {
    /** data **/
    private String path;

    /** components **/
    private EdgeAddEventStreamFromFile<KV,EV> stream;
    private TriangleCountState<KV,EV> state;

    /** storage **/
    private static final HazelcastInstance STORAGE = Hazelcast.newHazelcastInstance();

    public TriangleCount(String path){
        this.path = path;
        this.stream = new EdgeAddEventStreamFromFile<KV, EV>(path);
        state = new TriangleCountState<KV,EV>(STORAGE);
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
        String path = EdgeAddEventStreamFromFile.class.getClassLoader().getResource("").getPath() +"graph.txt";
        TriangleCount<Integer,Integer,Integer> triangleCount = new TriangleCount(path);
        triangleCount.run();
    }
}
