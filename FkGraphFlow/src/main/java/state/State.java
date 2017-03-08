package state;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created by SkyDream on 2017/3/8.
 */
public class State {

    public static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args) throws Exception {
        new State().test();
    }

    public  void test() throws Exception {
        env.setParallelism(8);
        env.getConfig().disableSysoutLogging();
        env.fromElements(1,1,1,1,1,2,2,2,2,2)
                .keyBy((KeySelector<Integer, Integer>) value -> value % 2)
                .map(new RuduceFunctionForState())
                .print();
        env.execute();
    }

    public class SumFunction extends RichMapFunction<Integer, Integer> {

        private transient ValueState<Integer> sum;

        public Integer map(Integer value) throws Exception {
            Integer currSum = sum.value();
            System.out.println("curr sum :" + currSum);
            Integer newSum = currSum + value;
            sum.update(newSum);
            return newSum;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
//            super.open(parameters);
            ValueStateDescriptor<Integer> descriptor = new ValueStateDescriptor<Integer>(
                    "sum",
                    TypeInformation.of(new TypeHint<Integer>() {}),
                    0
            );
            sum = getRuntimeContext().getState(descriptor);
        }
    }

    public class RuduceFunctionForState extends RichMapFunction<Integer,Integer>{
        private transient ReducingState<Integer> res;

        public Integer map(Integer value) throws Exception{
            res.add(value);
            return res.get();
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            ReducingStateDescriptor<Integer> descriptor = new ReducingStateDescriptor<Integer>(
                    "reduce",
                    new ReduceFunction<Integer>() {
                        @Override
                        public Integer reduce(Integer value1, Integer value2) throws Exception {
                            return value1 + value2;
                        }
                    },
                    TypeInformation.of(new TypeHint<Integer>() {})
            );
            res = getRuntimeContext().getReducingState(descriptor);
        }
    }
}
