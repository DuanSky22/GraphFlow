package com.duansky.hazelcast.graphflow.storage;

import com.hazelcast.core.HazelcastInstance;

/**
 * Created by SkyDream on 2017/3/17.
 */
public class StorageFactory {

    public static final HazelcastInstance getClient(){
        return HazelcastClient.getInstance().getClient();
    }

    public static final HazelcastInstance getServer(){
        return HazelcastServer.getInstance().getServer();
    }
}
