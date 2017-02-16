package com.duansky.hazelcast.graphflow.components;

/**
 * Created by SkyDream on 2017/2/16.
 */
public abstract class AbstractAlgorithm<ES extends EventStream, SE extends State> implements Algorithm {

    /** event stream and state **/
    protected ES stream;
    protected SE state;

    public AbstractAlgorithm(ES stream, SE state){
        this.stream = stream;
        this.state = state;
    }

    public ES getStream() {
        return stream;
    }

    public SE getState() {
        return state;
    }
}
