package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class JSONPath_5 extends TestCase {

    public void test_path() throws Exception {
        Model m = new Model();
        Value v = new Value(m);
        m.values.add(v);
        m.values.add(m.values);
        m.values.add(m);

        String json = JSON.toJSONString(m);
        System.out.println(json);
    }

    public static class Model {
        public List values = new ArrayList();
    }

    public static class Value {
        public Model model = new Model();

        public Value() {

        }

        public Value(Model model) {
            this.model = model;
        }
    }
}
