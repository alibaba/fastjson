package com.alibaba.json.bvt.parser.deser;

import java.util.Map;

import com.alibaba.fastjson.parser.ResolveFieldDeserializer;

import junit.framework.TestCase;


public class ResolveFieldDeserializerTest extends TestCase {
    public void test_0 () throws Exception {
        new ResolveFieldDeserializer((Map) null, null).parseField(null, null, null, null);
        new ResolveFieldDeserializer(null, null, 0).parseField(null, null, null, null);
    }
}
