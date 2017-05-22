package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

import junit.framework.TestCase;

public class Bug_for_issue_572_private extends TestCase {

    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.id = 1001;
        model.name = "wenshao";

        String text = JSON.toJSONString(model, SerializerFeature.WriteNonStringValueAsString);
        Assert.assertEquals("{\"id\":\"1001\",\"name\":\"wenshao\"}", text);
    }

    public void test_for_issue_1() throws Exception {
        Model model = new Model();
        model.id = 1001;
        model.name = "wenshao";

        ValueFilter valueFilter = new ValueFilter() {

            @Override
            public Object process(Object object, String name, Object value) {
                if (value instanceof Number) {
                    return null;
                }
                return value;
            }
        };

        String text = JSON.toJSONString(model, valueFilter, SerializerFeature.WriteNonStringValueAsString);
        Assert.assertEquals("{\"id\":\"1001\",\"name\":\"wenshao\"}", text);
    }

    private static class Model {

        private int    id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
