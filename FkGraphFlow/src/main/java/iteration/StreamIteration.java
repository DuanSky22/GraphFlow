package iteration;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created by SkyDream on 2017/3/15.
 */
public class StreamIteration {

    public static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    static{
        env.getConfig().disableSysoutLogging();
    }
    public static void main(String[] args) throws Exception {
        new StreamIteration().test0();
    }

    public void test0() throws Exception {
        DataStream<Long> someIntegers = env.fromElements(10L);

        IterativeStream<Long> iteration = someIntegers.iterate();

        DataStream<Long> minusOne = iteration.map(new MapFunction<Long, Long>() {
            @Override
            public Long map(Long value) throws Exception {
                return value - 1 ;
            }
        });

        DataStream<Long> stillGreaterThanZero = minusOne.filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long value) throws Exception {
                return (value > 0);
            }
        });

        iteration.closeWith(stillGreaterThanZero);

        DataStream<Long> lessThanZero = minusOne.filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long value) throws Exception {
                return (value <= 0);
            }
        });

        stillGreaterThanZero.print();
        env.execute();
    }
}
