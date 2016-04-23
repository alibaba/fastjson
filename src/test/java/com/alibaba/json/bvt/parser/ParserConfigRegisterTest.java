package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import junit.framework.TestCase;

public class ParserConfigRegisterTest extends TestCase {
    public void test_register() throws Exception {
        ObjectDeserializer obj = ParserConfig.getGlobalInstance().registerIfNotExists(Model.class);
        ObjectDeserializer obj2 = ParserConfig.getGlobalInstance().registerIfNotExists(Model.class);
        Assert.assertSame(obj, obj2);
    }
    
    public static class Model {
        public int id;
    }
}
