package util;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.graph.Graph;
import org.apache.flink.types.NullValue;

/**
 * Created by SkyDream on 2017/3/6.
 */
public class Graphs {

    public static Graph<Integer,NullValue,NullValue> generateGraph(ExecutionEnvironment env){
        return Graph.fromCsvReader(Contracts.EDGE_PATH,env).keyType(Integer.class);
    }
}
