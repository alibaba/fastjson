package com.alibaba.json.bvt.serializer.filters;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;

import junit.framework.TestCase;

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


        Bean a = new Bean();
        String text = JSON.toJSONString(a, filter);

        Assert.assertEquals("{\"id\":\"AAA\"}", text);
    }

    public static class Bean {

        public boolean    id;
        public String name;


    }
}
