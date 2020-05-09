package com.alibaba.json.bvt.parser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.json.bvt.parser.DefaultExtJSONParserTest.User;

public class DefaultExtJSONParserTest_2 extends TestCase {

    public void test_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{'a':3}");
        parser.config(Feature.AllowSingleQuotes, true);
        A a = parser.parseObject(A.class);
        Assert.assertEquals(3, a.getA());
    }

    public void test_1() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{a:3}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        A a = parser.parseObject(A.class);
        Assert.assertEquals(3, a.getA());
    }

    public void test_2() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{a:3}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        Map a = parser.parseObject(Map.class);
        Assert.assertEquals(3, a.get("a"));
    }

    public void test_3() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{a:3}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        HashMap a = parser.parseObject(HashMap.class);
        Assert.assertEquals(3, a.get("a"));
    }

    public void test_4() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{a:3}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        LinkedHashMap a = parser.parseObject(LinkedHashMap.class);
        Assert.assertEquals(3, a.get("a"));
    }

    public void test_error_0() throws Exception {
        Exception error = null;
        try {
            String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123]";
            DefaultJSONParser parser = new DefaultJSONParser(text);
            parser.parseArray(User.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        JSONException error = null;
        try {
            DefaultJSONParser parser = new DefaultJSONParser("{'a'3}");
            parser.config(Feature.AllowSingleQuotes, true);
            parser.parseObject(A.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        JSONException error = null;
        try {
            DefaultJSONParser parser = new DefaultJSONParser("{a 3}");
            parser.config(Feature.AllowUnQuotedFieldNames, true);
            parser.parseObject(A.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        JSONException error = null;
        try {
            DefaultJSONParser parser = new DefaultJSONParser("{");
            parser.config(Feature.AllowUnQuotedFieldNames, true);
            parser.parseObject(A.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        JSONException error = null;
        try {
            DefaultJSONParser parser = new DefaultJSONParser("{\"a\"3}");
            parser.parseObject(A.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_5() throws Exception {
        JSONException error = null;
        try {
            DefaultJSONParser parser = new DefaultJSONParser("{a:3}");
            parser.config(Feature.AllowUnQuotedFieldNames, false);
            parser.parseObject(A.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_6() throws Exception {
        JSONException error = null;
        try {
            DefaultJSONParser parser = new DefaultJSONParser("{'a':3}");
            parser.config(Feature.AllowSingleQuotes, false);
            parser.parseObject(A.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public static class A {

        private int a;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

    }

}
