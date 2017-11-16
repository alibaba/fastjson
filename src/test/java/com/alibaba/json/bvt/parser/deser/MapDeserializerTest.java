package com.alibaba.json.bvt.parser.deser;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class MapDeserializerTest extends TestCase {

    public void test_0() throws Exception {
        JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.parser.deser.MapDeserializerTest$MyMap\"}", Map.class, Feature.SupportAutoType);
    }
    
    public static class MyMap extends HashMap {
        public MyMap () {
            
        }
    }
}
