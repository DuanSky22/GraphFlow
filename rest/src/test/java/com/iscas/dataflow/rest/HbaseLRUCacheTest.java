package com.iscas.dataflow.rest;

import com.iscas.dataflow.controller.HbaseLRUCache;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by SkyDream on 2016/9/21.
 */

public class HbaseLRUCacheTest {

    HbaseLRUCache cache;

    @Before
    public void init(){
        cache = HbaseLRUCache.getInstance();
    }

    @Test
    public void testAdd(){
        cache.addToCache("www.baidu.com","baidu");
        cache.addToCache("www.google.com","google");
        cache.addToCache("www.baidu.com","baidu");
    }
}
