package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.serializer.*;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class NameFilterTest_short extends TestCase {

    public void test_namefilter() throws Exception {
        new FilterUtils();
        
        NameFilter filter = new NameFilter() {

            public String process(Object source, String name, DelayObject value) {
                if (value != null) {
					Object v = value.getValue();
					if(v != null)
                    	Assert.assertTrue(v instanceof Short);
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
        Assert.assertEquals("{\"ID\":0}", text);
    }

    public void test_namefilter_1() throws Exception {
        NameFilter filter = new NameFilter() {

            public String process(Object source, String name, DelayObject value) {
                if (name.equals("id")) {
					Object v = value.getValue();
                    Assert.assertTrue(v instanceof Short);
                    return "ID";
                }

                return name;
            }

        };

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getNameFilters().add(filter);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", (short) 0);
        serializer.write(map);

        String text = out.toString();
        Assert.assertEquals("{\"ID\":0}", text);
    }

    public static class Bean {

        private short    id;
        private String name;

        public short getId() {
            return id;
        }

        public void setId(short id) {
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
