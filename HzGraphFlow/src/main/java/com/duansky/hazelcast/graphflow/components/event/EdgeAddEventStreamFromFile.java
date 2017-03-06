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
 *
 * @param <KV> the type of the vertex.
 * @param <EV> the type of the edge value.
 */
public class EdgeAddEventStreamFromFile<KV,EV> implements EventStream<EventType,Edge<KV,EV>> {


    private String path;
    private EdgeReader edgeReader;
    private Class<KV> kvClass;
    private Class<EV> evClass;

    public EdgeAddEventStreamFromFile(String path,Class<KV> kvClass,Class<EV> evClass){
        super();
        this.path = path;
        edgeReader = new EdgeReader(path);
        types(kvClass,evClass);
    }

    public boolean hasNext() {
        return edgeReader.hasNext();
    }

    public EdgeEvent<KV,EV> next() {
        return edgeReader.next();
    }

    public void types(Class<KV> kvClass,Class<EV> evClass){
        this.kvClass = kvClass;
        this.evClass = evClass;
    }

    public String getPath() {
        return path;
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

            //=======================================================//
            // TODO how to change the String to the Generic Type?
//            edge.setSource((KV)data[0]);
//            edge.setTarget((KV)data[1]);
//            if(data.length > 2) edge.setEdgeValue((EV) data[2]);
            //=======================================================//

            edge.setSource((KV)transform(data[0],kvClass));
            edge.setTarget((KV)transform(data[1],kvClass));
            if(data.length > 2) edge.setEdgeValue((EV) transform(data[2],evClass));

            return new EdgeEvent<KV,EV>(type,edge);
        }

        public Object transform(String data,Class clazz){
            if(clazz.equals(Integer.class))  return Integer.parseInt(data);
            else if(clazz.equals(Long.class)) return Long.parseLong(data);
            else if(clazz.equals(Double.class)) return Double.parseDouble(data);
            else if(clazz.equals(Number.class)) return Long.parseLong(data);
            return data;
        }
    }

    public static void main(String args[]){
        String path = EdgeAddEventStreamFromFile.class.getClassLoader().getResource("").getPath() +"graph.txt";
        EdgeAddEventStreamFromFile<Integer,Integer> stream = new EdgeAddEventStreamFromFile<Integer, Integer>(path,Integer.class,Integer.class);
        while(stream.hasNext())
            System.out.println(stream.next());
    }

}
