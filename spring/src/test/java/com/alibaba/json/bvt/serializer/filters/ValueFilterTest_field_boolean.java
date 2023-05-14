package com.alibaba.json.bvt.serializer.filters;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.ValueFilter;

public class ValueFilterTest_field_boolean extends TestCase {

    public void test_valuefilter() throws Exception {
        ValueFilter filter = new ValueFilter() {

            public Object process(Object source, String name, Object value) {
                if (name.equals("id")) {
                    return "AAA";
                }

                return value;
            }

        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getValueFilters().add(filter);

        Bean a = new Bean();
        serializer.write(a);

        String text = out.toString();
        Assert.assertEquals("{\"id\":\"AAA\"}", text);
    }
    
    public void test_toJSONString() throws Exception {
        ValueFilter filter = new ValueFilter() {

            public Object process(Object source, String name, Object value) {
                if (name.equals("id")) {
                    return "AAA";
                }

                return value;
            }

        };
        
        Assert.assertEquals("{\"id\":\"AAA\"}", JSON.toJSONString(new Bean(), filter));
    }

    public void test_valuefilter_1() throws Exception {
        ValueFilter filter = new ValueFilter() {

            public Object process(Object source, String name, Object value) {
                if (name.equals("name")) {
                    return "AAA";
                }

                return value;
            }

        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getValueFilters().add(filter);

        Bean a = new Bean();
        serializer.write(a);

        String text = out.toString();
        Assert.assertEquals("{\"id\":false,\"name\":\"AAA\"}", text);
    }

    public void test_valuefilter_2() throws Exception {
        ValueFilter filter = new ValueFilter() {

            public Object process(Object source, String name, Object value) {
                if (name.equals("name")) {
                    return "AAA";
                }

                return value;
            }

        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getValueFilters().add(filter);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", null);
        serializer.write(map);

        String text = out.toString();
        Assert.assertEquals("{\"name\":\"AAA\"}", text);
    }

    public void test_valuefilter_3() throws Exception {
        ValueFilter filter = new ValueFilter() {

            public Object process(Object source, String name, Object value) {
                if (name.equals("name")) {
                    return null;
                }

                return value;
            }

        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getValueFilters().add(filter);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "AA");
        serializer.write(map);

        String text = out.toString();
        Assert.assertEquals("{}", text);
    }

    public static class Bean {

        public boolean    id;
        public String name;


    }
}
