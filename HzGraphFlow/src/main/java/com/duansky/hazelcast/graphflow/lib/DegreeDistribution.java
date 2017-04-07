package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.AbstractAlgorithm;
import com.duansky.hazelcast.graphflow.components.event.EdgeAddEventStreamFromFile;
import com.duansky.hazelcast.graphflow.components.event.EdgeEvent;
import com.duansky.hazelcast.graphflow.components.event.EventType;
import com.duansky.hazelcast.graphflow.graph.Edge;
import com.duansky.hazelcast.graphflow.storage.StorageFactory;
import com.duansky.hazelcast.graphflow.util.Files;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

import static com.duansky.hazelcast.graphflow.util.Contracts.TEST_BASE;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class DegreeDistribution<KV,EV> extends AbstractAlgorithm<EdgeAddEventStreamFromFile<KV,EV>,DegreeDistributionState<KV,EV>>{

    /** private name **/
    private String name;

    /** data **/
    private String path;

    /** storage **/
    private static final HazelcastInstance STORAGE = StorageFactory.getClient();

    public DegreeDistribution(String name,String path,Class<KV> kvClass,Class<EV> evClass){
        super(new EdgeAddEventStreamFromFile<KV, EV>(path,kvClass,evClass),new DegreeDistributionState<KV,EV>(name,STORAGE));
        this.path = path;
        this.name = name;
    }

    public void run(){
        System.out.println("start the degree distribution algorithm.");
        long start,end,counter = 1;
        PrintWriter writer = Files.asPrintWriter(TEST_BASE+ File.separator+"dd.txt");
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
        DegreeDistribution<Integer,Integer> distribution = new DegreeDistribution("test",path,Integer.class,Integer.class);
        distribution.run();
    }

}
