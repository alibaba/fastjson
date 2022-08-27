package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_yangqi extends TestCase {

    public void test_for_bug() throws Exception {
        B b = JSON.parseObject("{\"id\":123,\"values\":[{}]}", B.class);
    }

    abstract static class A {

        private int id;
        private List<Value> values = new ArrayList<Value>();

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

    public static class B extends A {

    }
    
    public static class Value {
        
    }
}
