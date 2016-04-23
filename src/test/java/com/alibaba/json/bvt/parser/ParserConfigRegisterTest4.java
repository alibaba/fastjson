package com.alibaba.json.bvt.parser;

import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import junit.framework.TestCase;

public class ParserConfigRegisterTest4 extends TestCase {

    public void test_register() throws Exception {
        ObjectDeserializer obj = ParserConfig.getGlobalInstance() //
                                             .registerIfNotExists(Model.class);
        ObjectDeserializer obj2 = ParserConfig.getGlobalInstance().registerIfNotExists(Model.class);
        Assert.assertSame(obj, obj2);
    }

    public static class Model {

        private int id;
        private List<Value> values;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        
        public List<Value> getValues() {
            return values;
        }

        
        public void setValues(List<Value> values) {
            this.values = values;
        }

        
    }
    
    public static class Value {
        
    }
}
