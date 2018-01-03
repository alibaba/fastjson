package com.alibaba.json.bvt.parser.deser.generic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

public class GenericTest5 extends TestCase {
    public void test_generic() {
        Pair<Long> p1 = new Pair<Long>();
        p1.label = "p1";
        p1.value = 3L;
        String p1Json = JSON.toJSONString(p1);

        JSON.parseObject(p1Json, LongPair.class);
        JSON.parseObject(p1Json, StringPair.class);

        JSONObject p1Jobj = JSON.parseObject(p1Json);

        TypeReference<Pair<Long>> tr = new TypeReference<Pair<Long>>() {};
        Pair<Long> p2 = null;
        p2 = JSON.parseObject(p1Json, tr);
        assertNotNull(p2); // 基于JSON字符串的转化正常

        p2 = p1Jobj.toJavaObject(tr);
        assertNotNull(p2);
        assertEquals(Long.valueOf(3), p2.value);


    }

    public static class Pair<T> {
        private T value;
        public String label;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    public static class LongPair extends Pair<String> {

    }

    public static class StringPair extends Pair<String> {

    }
}
