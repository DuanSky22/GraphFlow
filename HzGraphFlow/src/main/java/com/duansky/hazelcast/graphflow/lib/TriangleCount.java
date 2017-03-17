package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.AbstractAlgorithm;
import com.duansky.hazelcast.graphflow.components.event.EdgeAddEventStreamFromFile;
import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.IndividualState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.storage.StorageFactory;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class TriangleCount<KV,EV,VV> extends AbstractAlgorithm<EdgeAddEventStreamFromFile<KV,EV>,TriangleCountState<KV,EV>>{
    /** data **/
    private String path;

    private String name;

    /** storage **/
    private static final HazelcastInstance STORAGE = StorageFactory.getClient();

    public TriangleCount(String name,String path,Class<KV> kvClass,Class<EV> evClass){
        super(new EdgeAddEventStreamFromFile<KV, EV>(path,kvClass,evClass),new TriangleCountState<KV,EV>(name,STORAGE));
        this.path = path;
        this.name = name;
    }

    public void run(){
        long start = System.currentTimeMillis();
        while(stream.hasNext()) {
            System.out.println("==============");
            EdgeEvent<KV, EV> edgeEvent = stream.next();
            state.update(edgeEvent);
            System.out.println(state.get());
        }
        System.out.println("Time:"+ (System.currentTimeMillis()-start));
    }
    public static void main(String args[]){
        String path = EdgeAddEventStreamFromFile.class.getClassLoader().getResource("").getPath() +"graph-1000-0.1-p2.txt";
        TriangleCount<Integer,Integer,Integer> triangleCount = new TriangleCount("test1",path,Integer.class,Integer.class);
        triangleCount.run();
    }
}
