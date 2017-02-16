package com.duansky.hazelcast.graphflow.util;

/**
 * Created by SkyDream on 2017/2/15.
 */
public interface Contracts {

    /** state storage*/
    String NEIGHBORHOOD_STATE = "neighbors";
    String NEIGHBORHOOD_WITH_EDGE_VALUE_STATE = "neighbor-with-edge-values";
    String DEGREE_DISTRIBUTION_STATE = "degree-distribution";

    String TRIANGLE_COUNT_STATE = "triangle-count";
    String SSSP_STATE = "single-source-shortest-path";
}
