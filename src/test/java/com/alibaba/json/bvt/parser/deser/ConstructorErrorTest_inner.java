package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class ConstructorErrorTest_inner extends TestCase {

    public void test_error() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("value", new JSONObject());
        Exception error = null;
        try {
            obj.toJavaObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {
        public Value value;

        public Model(){
        }
        
        public class Value {
            public Value() {
                throw new IllegalStateException();
            }
        }
    }
    
}
