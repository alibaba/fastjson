package com.alibaba.json.bvt.parser.deser;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class MapDeserializerTest extends TestCase {
    protected void setUp() throws Exception {
        com.alibaba.fastjson.parser.ParserConfig.global.addAccept("com.alibaba.json.bvt.parser.deser.MapDeserializerTest.");
    }

    public void test_0() throws Exception {
        JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.parser.deser.MapDeserializerTest$MyMap\"}", Map.class);
    }
    
    public static class MyMap extends HashMap {
        public MyMap () {
            
        }
    }
}
