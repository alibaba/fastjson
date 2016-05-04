package com.alibaba.json.bvt.serializer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class ListFieldTest extends TestCase {

    public void test_for_list() throws Exception {
        Model model = new Model();
        model.id = 1000;
        Assert.assertEquals("{\"id\":1000,\"values\":[]}", JSON.toJSONString(model));
        
        model.values.add("1001");
        Assert.assertEquals("{\"id\":1000,\"values\":[\"1001\"]}", JSON.toJSONString(model));
        
        model.values.add("1002");
        Assert.assertEquals("{\"id\":1000,\"values\":[\"1001\",\"1002\"]}", JSON.toJSONString(model));
        
        model.values.add("1003");
        Assert.assertEquals("{\"id\":1000,\"values\":[\"1001\",\"1002\",\"1003\"]}", JSON.toJSONString(model));
    }

    public static class Model {

        private int          id;
        private List<String> values = new ArrayList<String>();

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<String> getValues() {
            return values;
        }

    }
}
