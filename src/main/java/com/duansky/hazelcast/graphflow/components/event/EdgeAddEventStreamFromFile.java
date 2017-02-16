package com.duansky.hazelcast.graphflow.components.event;

import com.duansky.hazelcast.graphflow.components.EventStream;
import com.duansky.hazelcast.graphflow.graph.Edge;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Created by SkyDream on 2017/2/15.
 * The {@link EdgeAddEventStreamFromFile} will read the edge data from a file.
 */
public class EdgeAddEventStreamFromFile<KV,EV> implements EventStream<EventType,Edge<KV,EV>> {


    private String path;
    private EdgeReader edgeReader;

    public EdgeAddEventStreamFromFile() {
        super();
    }

    public EdgeAddEventStreamFromFile(String path){
        super();
        this.path = path;
        edgeReader = new EdgeReader(path);
    }

    public boolean hasNext() {
        return edgeReader.hasNext();
    }

    public EdgeEvent<KV,EV> next() {
        return edgeReader.next();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        edgeReader = new EdgeReader(path);
    }

    private class EdgeReader{

        LineNumberReader reader;
        String currentLine;

        public EdgeReader(String path){
            try {
                this.reader = new LineNumberReader(new FileReader(path));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public boolean hasNext(){
            try {
                currentLine = reader.readLine();
                if(currentLine == null) {
                    reader.close();
                    return false;
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        public EdgeEvent<KV,EV> next(){
            String[] data = currentLine.split(",");

            //event type
            EventType type = EventType.ADD;

            //event value
            Edge<KV,EV> edge = new Edge<KV, EV>();
            edge.setSource((KV)data[0]);
            edge.setTarget((KV)data[1]);
            if(data.length > 2) edge.setEdgeValue((EV) data[2]);

            return new EdgeEvent<KV,EV>(type,edge);
        }
    }

    public static void main(String args[]){
        String path = EdgeAddEventStreamFromFile.class.getClassLoader().getResource("").getPath() +"graph.txt";
        EdgeAddEventStreamFromFile<Integer,Integer> stream = new EdgeAddEventStreamFromFile<Integer, Integer>(path);
        while(stream.hasNext())
            System.out.println(stream.next());
    }

}
