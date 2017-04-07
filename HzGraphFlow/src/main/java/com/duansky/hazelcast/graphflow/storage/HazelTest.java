package com.duansky.hazelcast.graphflow.storage;

import com.hazelcast.client.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;

/**
 * Created by SkyDream on 2017/3/22.
 */
public class HazelTest {
    public static void main(String[] args) {
        get();
    }

    public static void test(){
        HazelcastInstance hi = HazelcastClient.getInstance().getClient();
        IAtomicLong counter = hi.getAtomicLong("counter");
        for(int i = 0; i < 100000; i++){
            System.out.println(i);
            counter.addAndGet(1);
        }
    }

    public static void get(){
        HazelcastInstance hi = HazelcastClient.getInstance().getClient();
        System.out.println(hi.getAtomicLong("counter").get());
    }
}
