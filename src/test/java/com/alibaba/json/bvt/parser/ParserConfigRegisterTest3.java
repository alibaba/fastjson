package com.alibaba.json.bvt.parser;

import java.lang.reflect.Modifier;

import org.junit.Assert;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import junit.framework.TestCase;

public class ParserConfigRegisterTest3 extends TestCase {

    public void test_register() throws Exception {
        ObjectDeserializer obj = ParserConfig.getGlobalInstance() //
                                             .registerIfNotExists(Model.class, Modifier.PUBLIC, false, false, false,
                                                                  false);
        ObjectDeserializer obj2 = ParserConfig.getGlobalInstance().registerIfNotExists(Model.class);
        Assert.assertSame(obj, obj2);
    }

    public static class Model {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
