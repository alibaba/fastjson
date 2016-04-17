package com.alibaba.json.bvt.serializer.filters;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class NameFilterTest_boolean extends TestCase {

    public void test_namefilter() throws Exception {
        NameFilter filter = new NameFilter() {

            public String process(Object source, String name, Object value) {
                if (value != null) {
                    Assert.assertTrue(value instanceof Boolean);
                }
                
                if (name.equals("id")) {
                    return "ID";
                }

                return name;
            }

        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getNameFilters().add(filter);

        Bean a = new Bean();
        serializer.write(a);

        String text = out.toString();
        Assert.assertEquals("{\"ID\":false}", text);
    }

    public void test_namefilter_1() throws Exception {
        NameFilter filter = new NameFilter() {

            public String process(Object source, String name, Object value) {
                if (name.equals("id")) {
                    return "ID";
                }

                return name;
            }

        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getNameFilters().add(filter);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", true);
        serializer.write(map);

        String text = out.toString();
        Assert.assertEquals("{\"ID\":true}", text);
    }

    public static class Bean {

        private boolean id;
        private String  name;

        public boolean isId() {
            return id;
        }

        public void setId(boolean id) {
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
