package com.alibaba.json.bvt.serializer.filters;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class NameFilterTest_long extends TestCase {

    public void test_namefilter() throws Exception {
        NameFilter filter = new NameFilter() {

            public String process(Object source, String name, Object value) {
                if (name.equals("id")) {
                    Assert.assertTrue(value instanceof Long);
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
        Assert.assertEquals("{\"ID\":0}", text);
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
        map.put("id", 0);
        serializer.write(map);

        String text = out.toString();
        Assert.assertEquals("{\"ID\":0}", text);
    }

    public static class Bean {

        private long   id;
        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
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
