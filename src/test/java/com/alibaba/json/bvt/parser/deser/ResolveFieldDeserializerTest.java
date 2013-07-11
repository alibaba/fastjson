package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import com.alibaba.fastjson.parser.deserializer.ListResolveFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.MapResolveFieldDeserializer;


public class ResolveFieldDeserializerTest extends TestCase {
    public void test_0 () throws Exception {
        new MapResolveFieldDeserializer(null, null).parseField(null, null, null, null);
        new ListResolveFieldDeserializer(null, null, 0).parseField(null, null, null, null);
    }
}
