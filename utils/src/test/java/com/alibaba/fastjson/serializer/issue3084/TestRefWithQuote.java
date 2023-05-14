package com.alibaba.fastjson.serializer.issue3084;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestRefWithQuote {

    public static class X {
        private String x;

        public X(String x) {
            this.x = x;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }
    }

    @Test
    public void testIssue3084() {
        Map<String, TestRefWithQuote.X> origin = new HashMap<String, X>();
        TestRefWithQuote.X x = new TestRefWithQuote.X("x");
        origin.put("aaaa\"", x);
        origin.put("bbbb\"", x);

        try {
            String json = JSON.toJSONString(origin, true);
            JSONObject root = JSON.parseObject(json);
            assertSame(root.get("bbbb\\"), root.get("aaaa\\"));
        } catch (Exception e) {
            fail("should not fail !!!");
        }
    }
}
