package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/05/2017.
 */
public class Issue1226 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"c\":\"c\"}";
        TestBean tb1 = JSON.parseObject(json, TestBean.class);
        assertEquals('c', tb1.getC());

        TestBean2 tb2 = JSON.parseObject(json, TestBean2.class);
        assertEquals('c', tb2.getC().charValue());

        String json2 = JSON.toJSONString(tb2);
        JSONObject jo = JSON.parseObject(json2);

        TestBean tb12 = jo.toJavaObject(TestBean.class);
        assertEquals('c', tb12.getC());

        TestBean2 tb22 = jo.toJavaObject(TestBean2.class);
        assertEquals('c', tb22.getC().charValue());
    }

    static class TestBean{
        char c;

        public char getC() {
            return c;
        }

        public void setC(char c) {
            this.c = c;
        }
    }

    static class TestBean2{
        Character c;

        public Character getC() {
            return c;
        }

        public void setC(Character c) {
            this.c = c;
        }
    }
}
