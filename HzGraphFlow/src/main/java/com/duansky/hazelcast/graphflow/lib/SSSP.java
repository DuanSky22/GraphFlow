package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.AbstractAlgorithm;
import com.duansky.hazelcast.graphflow.components.event.EdgeAddEventStreamFromFile;
import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.storage.StorageFactory;
import com.duansky.hazelcast.graphflow.util.Files;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

import static com.duansky.hazelcast.graphflow.util.Contracts.TEST_BASE;

/**
 * Created by SkyDream on 2017/2/16.
 */
public class SSSP<KV,EV extends Number> extends AbstractAlgorithm<EdgeAddEventStreamFromFile<KV,EV>,SSSPState<KV,EV>>{

    /** data **/
    private String path;
    private boolean directed;
    private String name;

    /** storage **/
    private static final HazelcastInstance STORAGE = StorageFactory.getClient();

    public SSSP(String name,String path,KV original,boolean directed,Class<KV> kvClass,Class<EV> evClass){
        super(new EdgeAddEventStreamFromFile<KV, EV>(path,kvClass,evClass),new SSSPState<KV,EV>(name,STORAGE,original));
        this.path = path;
        this.directed = directed;
        this.name = name;
    }

    public void run() {
        System.out.println("start the single source shortest path algorithm.");
        long start,end,counter = 1;
        PrintWriter writer = Files.asPrintWriter(TEST_BASE+ File.separator+"sssp.txt");
        while(stream.hasNext()){
            start = System.currentTimeMillis();

            EdgeEvent<KV,EV> event = stream.next();
            state.update(event);
            if(!directed) state.update(new EdgeEvent<KV, EV>(event.getType(),event.getValue().reverse()));

            end = System.currentTimeMillis();

            writer.append((end-start)+"\n");
            writer.flush();
            System.out.println("edge:"+(counter++)+"\ttime:"+(end-start));
        }
    }

    public static void main(String args[]){
        String path = EdgeAddEventStreamFromFile.class.getClassLoader().getResource("").getPath() +"graph-1000-0.1-p2.txt";
        SSSP<Integer,Integer> distribution = new SSSP("test",path,1,true,Integer.class,Integer.class);
        distribution.run();
    }
}
