package com.alibaba.json.bvt.bug;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_smoothrat9 extends TestCase {

    public void test_set() throws Exception {
        Map<Integer, Object> map = new LinkedHashMap<Integer, Object>();
        map.put(1, "a");
        map.put(2, "b");


        String text = JSON.toJSONString(map, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"java.util.LinkedHashMap\",1:\"a\",2:\"b\"}",
                            text);

        Map<Integer, Object> value = (Map<Integer, Object>) JSON.parse(text);
        Assert.assertEquals(map, value);
        Assert.assertEquals(map.getClass(), value.getClass());
        Assert.assertEquals(Integer.class, value.keySet().iterator().next().getClass());
    }
    

    public static class Entity {

        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
