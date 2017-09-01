package com.alibaba.json.bvt.parser;

import java.lang.reflect.Modifier;

import org.junit.Assert;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import junit.framework.TestCase;

public class ParserConfigRegisterTest2 extends TestCase {
    public void test_register() throws Exception {
        assertFalse(ParserConfig.getGlobalInstance().containsKey(Model.class));
        ObjectDeserializer obj = ParserConfig.getGlobalInstance() //
                .registerIfNotExists(Model.class, Modifier.PUBLIC, true, false, false, false);
        ObjectDeserializer obj2 = ParserConfig.getGlobalInstance().registerIfNotExists(Model.class);
        Assert.assertSame(obj, obj2);
        assertTrue(ParserConfig.getGlobalInstance().containsKey(Model.class));
    }
    
    public static class Model {
        public int id;
    }
}
