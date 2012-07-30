package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.serializer.DelayObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class NameFilterTest_boolean extends TestCase {

    public void test_namefilter() throws Exception {
        NameFilter filter = new NameFilter() {

            public String process(Object source, String name, DelayObject value) {
                if (value != null) {
					Object v = value.getValue();
					if(v != null)
                    	Assert.assertTrue(v instanceof Boolean);
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

            public String process(Object source, String name, DelayObject value) {
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
