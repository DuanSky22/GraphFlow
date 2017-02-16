package com.duansky.hazelcast.graphflow.graph;

/**
 * Created by SkyDream on 2017/2/15.
 */
public class Vertex<KV,VV> {

    private KV id;
    private VV vertexValue;

    public Vertex(){}

    public Vertex(KV id, VV vertexValue){
        this.id = id;
        this.vertexValue = vertexValue;
    }

    public KV getId() {
        return id;
    }

    public void setId(KV id) {
        this.id = id;
    }

    public VV getVertexValue() {
        return vertexValue;
    }

    public void setVertexValue(VV vertexValue) {
        this.vertexValue = vertexValue;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + id +
                ", vertexValue=" + vertexValue +
                '}';
    }
}
