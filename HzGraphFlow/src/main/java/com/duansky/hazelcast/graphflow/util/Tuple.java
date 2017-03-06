package com.duansky.hazelcast.graphflow.util;

/**
 * Created by SkyDream on 2017/2/16.
 */
public class Tuple<U,V> {
    U f0;
    V f1;
    public Tuple(){}

    public Tuple(U f0,V f1){
        this.f0 = f0;
        this.f1 = f1;
    }

    public U getF0() {
        return f0;
    }

    public void setF0(U f0) {
        this.f0 = f0;
    }

    public V getF1() {
        return f1;
    }

    public void setF1(V f1) {
        this.f1 = f1;
    }
}
