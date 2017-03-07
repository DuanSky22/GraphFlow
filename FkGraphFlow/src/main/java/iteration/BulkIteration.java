package iteration;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.IterativeDataSet;

/**
 * Created by SkyDream on 2017/3/7.
 */
public class BulkIteration {

    public static final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

    public static void main(String args[]) throws Exception {
        env.getConfig().disableSysoutLogging();
        BulkIteration t = new BulkIteration();
        t.test2();
    }

    public void testBulkIteration() throws Exception {
//        env.getConfig().disableSysoutLogging();
        //initial the dataset.
        IterativeDataSet<Integer> initial = env.fromElements(0).iterate(10000);

        //define the step function.
        DataSet<Integer> iteration = initial.map(new MapFunction<Integer, Integer>() {
            public Integer map(Integer value) throws Exception {
                double x = Math.random();
                double y = Math.random();

                return value + (( x * x + y * y < 1 ) ? 1 : 0);
            }
        });

        // Iteratively transform the IterativeDataSet
        DataSet<Integer> count = initial.closeWith(iteration);

        count.map(new MapFunction<Integer, Double>() {
            public Double map(Integer count) throws Exception {
                return count / (double) 10000 * 4;
            }
        }).print();

    }

    public void test2() throws Exception {
        //define the data source.
        DataSet<Integer> dataSource = env.fromElements(1,2,3,4,5);
        DataSet<Integer> crition = dataSource.filter(new FilterFunction<Integer>() {
            public boolean filter(Integer value) throws Exception {
                return value % 2 == 0;
            }
        });
        //define the iterative dataset.
        IterativeDataSet<Integer> iterativeDataSet = dataSource.iterate(100);

        //define the step function.
        DataSet<Integer> stepFunction = iterativeDataSet.map(new MapFunction<Integer, Integer>() {
            public Integer map(Integer value) throws Exception {
                return value+1;
            }
        });

        //iterative dataset close with step function.
        iterativeDataSet.closeWith(stepFunction,crition).print();
    }
}
