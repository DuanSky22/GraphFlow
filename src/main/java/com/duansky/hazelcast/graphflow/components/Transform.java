package com.duansky.hazelcast.graphflow.components;

/**
 * Created by SkyDream on 2017/2/15.
 */
public interface Transform<E extends Event,S extends State> {
    void update(E e,S s);
}
