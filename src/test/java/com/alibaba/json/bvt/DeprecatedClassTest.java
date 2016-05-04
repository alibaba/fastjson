package com.alibaba.json.bvt;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JSONSerializerMap;

import junit.framework.TestCase;

@SuppressWarnings("deprecation")
public class DeprecatedClassTest extends TestCase {
    @SuppressWarnings("resource")
    public void test_0() throws Exception {
        new DefaultExtJSONParser("");
        new DefaultExtJSONParser("", ParserConfig.getGlobalInstance(), 1);
        new DefaultExtJSONParser("".toCharArray(), 0, ParserConfig.getGlobalInstance(), 1);
    }
    
    public void test_1() throws Exception {
        new JSONSerializerMap().put(Object.class, null);
    }
}
