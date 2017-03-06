package com.duansky.flink.graph.basictest;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.graph.Edge;
import org.apache.flink.graph.Graph;
import org.apache.flink.graph.Vertex;
import org.apache.flink.graph.pregel.ComputeFunction;
import org.apache.flink.graph.pregel.MessageCombiner;
import org.apache.flink.graph.pregel.MessageIterator;
import org.apache.flink.types.NullValue;
import org.junit.Test;
import util.Graphs;

/**
 * Created by SkyDream on 2017/3/6.
 */
public class VertexCentricIterationT {
    private static Integer srcId = 1;

    @Test
    public void testVC(){
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().disableSysoutLogging();
        Graph<Integer,NullValue,NullValue> graph = Graphs.generateGraph(env);
        try {
            long count = graph.getDegrees().count();
            System.out.println(count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // - - -  UDFs - - - //

    public static final class SSSPComputeFunction extends ComputeFunction<Long, Double, Double, Double> {

        public void compute(Vertex<Long, Double> vertex, MessageIterator<Double> messages) {

            double minDistance = (vertex.getId().equals(srcId)) ? 0d : Double.POSITIVE_INFINITY;

            for (Double msg : messages) {
                minDistance = Math.min(minDistance, msg);
            }

            if (minDistance < vertex.getValue()) {
                setNewVertexValue(minDistance);
                for (Edge<Long, Double> e : getEdges()) {
                    sendMessageTo(e.getTarget(), minDistance + e.getValue());
                }
            }
        }
    }

        // message combiner
    public static final class SSSPCombiner extends MessageCombiner<Long, Double> {

        public void combineMessages(MessageIterator<Double> messages) {

            double minMessage = Double.POSITIVE_INFINITY;
            for (Double msg: messages) {
                minMessage = Math.min(minMessage, msg);
            }
            sendCombinedMessage(minMessage);
        }
     }
}
