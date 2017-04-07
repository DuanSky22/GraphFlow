package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.AbstractAlgorithm;
import com.duansky.hazelcast.graphflow.components.event.EdgeAddEventStreamFromFile;
import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.components.state.IndividualState;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.storage.StorageFactory;
import com.duansky.hazelcast.graphflow.util.Files;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.File;
import java.io.PrintWriter;

import static com.duansky.hazelcast.graphflow.util.Contracts.TEST_BASE;

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
        System.out.println("start the triangle count algorithm.");
        long start,end,counter = 1;
        PrintWriter writer = Files.asPrintWriter(TEST_BASE+ File.separator+"tc.txt");
        while(stream.hasNext()){
            start = System.currentTimeMillis();
            state.update(stream.next());
            end = System.currentTimeMillis();
            writer.append((end-start)+"\n");
            writer.flush();
            System.out.println("edge:"+(counter++)+"\ttime:"+(end-start));
        }
    }
    public static void main(String args[]){
        String path = EdgeAddEventStreamFromFile.class.getClassLoader().getResource("").getPath() +"graph-1000-0.1-p2.txt";
        TriangleCount<Integer,Integer,Integer> triangleCount = new TriangleCount("test1",path,Integer.class,Integer.class);
        triangleCount.run();
    }
}
