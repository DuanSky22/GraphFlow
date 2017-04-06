package com.duansky.hazelcast.graphflow.util;

import java.io.File;

/**
 * Created by SkyDream on 2017/2/15.
 */
public interface Contracts {

    /** state storage*/
    String OUT_NEIGHBORHOOD_STATE = "out-neighbors";
    String OUT_NEIGHBORHOOD_WITH_EDGE_VALUE_STATE = "out-neighbor-with-edge-values";
    String IN_OUT_NEIGHBORHOOD_STATE = "in-out-neighbors";
    String VERTEX_NUMBER_STATE = "vertex-numbers";

    String DEGREE_DISTRIBUTION_STATE = "degree-distribution";
    String TRIANGLE_COUNT_STATE = "triangle-count";
    String SSSP_STATE = "single-source-shortest-path";
    String PAGERANK_STATE = "pagerank";

    /** test **/
    String TEST_BASE = System.getProperty("user.dir") + File.separator + "test";
}
