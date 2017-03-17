package com.duansky.hazelcast.graphflow.lib;

import com.duansky.hazelcast.graphflow.components.AbstractAlgorithm;
import com.duansky.hazelcast.graphflow.components.event.EdgeAddEventStreamFromFile;
import com.duansky.hazelcast.graphflow.storage.StorageFactory;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

/**
 * Created by SkyDream on 2017/2/20.
 */
public class PageRank<KV,EV> extends AbstractAlgorithm<EdgeAddEventStreamFromFile<KV,EV>,PageRankState<KV,EV>> {

    /** algorithm parameters **/
    private double delta = 0.2;
    private int maxIteration = 10000;
    private String path;
    private String name;

    /** storage **/
    private static final HazelcastInstance STORAGE = StorageFactory.getClient();

    public PageRank(String name,String path,Class<KV> kvClass,Class<EV> evClass, double delta,int maxIteration) {
        super(new EdgeAddEventStreamFromFile<KV, EV>(path,kvClass,evClass), new PageRankState<KV,EV>(name,STORAGE,delta,maxIteration));
        this.path = path;
        this.delta = delta;
        this.maxIteration = maxIteration;
        this.name = name;
    }

    @Override
    public void run() {
        while(stream.hasNext()){
            state.update(stream.next());
            System.out.println("==============");
            Map<KV,Double> prs = state.getCurrentState();
            for(Map.Entry entry : prs.entrySet())
                System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    public static void main(String args[]){
        String path = PageRank.class.getClassLoader().getResource("").getPath() +"graph-1000-0.1-p2.txt";
        double delta = 0.5;
        int maxIteration = 100;
        PageRank<Integer,Integer> pageRank = new PageRank("test",path,Integer.class,Integer.class,delta,maxIteration);
        pageRank.run();
    }
}
