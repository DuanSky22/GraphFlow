package com.duansky.hazelcast.graphflow.graph;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class Edge<KV,EV>{

    private KV source;
    private KV target;
    private EV edgeValue;

    public Edge(){}

    public Edge(KV source, KV target, EV edgeValue){
        this.source = source;
        this.target = target;
        this.edgeValue = edgeValue;
    }

    public KV getSource() {
        return source;
    }

    public void setSource(KV source) {
        this.source = source;
    }

    public KV getTarget() {
        return target;
    }

    public void setTarget(KV target) {
        this.target = target;
    }

    public EV getEdgeValue() {
        return edgeValue;
    }

    public void setEdgeValue(EV edgeValue) {
        this.edgeValue = edgeValue;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", target=" + target +
                ", edgeValue=" + edgeValue +
                '}';
    }
}
