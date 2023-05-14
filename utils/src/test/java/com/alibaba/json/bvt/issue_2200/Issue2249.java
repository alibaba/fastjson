package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class Issue2249 extends TestCase {
    public void test_for_issue() throws Exception {
        assertSame(Type.Big, JSON.parseObject("\"big\"", Type.class));
        assertSame(Type.Big, JSON.parseObject("\"Big\"", Type.class));
        assertSame(Type.Big, JSON.parseObject("\"BIG\"", Type.class));
        assertSame(Type.Small, JSON.parseObject("\"Small\"", Type.class));
        assertSame(Type.Small, JSON.parseObject("\"small\"", Type.class));
        assertSame(Type.Small, JSON.parseObject("\"SMALL\"", Type.class));
        assertSame(Type.Medium, JSON.parseObject("\"medium\"", Type.class));
        assertSame(Type.Medium, JSON.parseObject("\"MEDIUM\"", Type.class));
        assertSame(Type.Medium, JSON.parseObject("\"Medium\"", Type.class));
        assertSame(Type.Medium, JSON.parseObject("\"MediuM\"", Type.class));
        assertNull(JSON.parseObject("\"\"", Type.class));
    }

    public void test_for_issue_1() throws Exception {
        assertSame(Type.Big, JSON.parseObject("{\"type\":\"bIG\"}", Model.class).type);
        assertSame(Type.Big, JSON.parseObject("{\"type\":\"big\"}", Model.class).type);
        assertSame(Type.Big, JSON.parseObject("{\"type\":\"Big\"}", Model.class).type);
        assertSame(Type.Big, JSON.parseObject("{\"type\":\"BIG\"}", Model.class).type);

        assertSame(Type.Small, JSON.parseObject("{\"type\":\"Small\"}", Model.class).type);
        assertSame(Type.Small, JSON.parseObject("{\"type\":\"SmAll\"}", Model.class).type);
        assertSame(Type.Small, JSON.parseObject("{\"type\":\"small\"}", Model.class).type);
        assertSame(Type.Small, JSON.parseObject("{\"type\":\"SMALL\"}", Model.class).type);

        assertSame(Type.Medium, JSON.parseObject("{\"type\":\"Medium\"}", Model.class).type);
        assertSame(Type.Medium, JSON.parseObject("{\"type\":\"MediuM\"}", Model.class).type);
        assertSame(Type.Medium, JSON.parseObject("{\"type\":\"medium\"}", Model.class).type);
        assertSame(Type.Medium, JSON.parseObject("{\"type\":\"MEDIUM\"}", Model.class).type);

    }

    public void test_for_issue_null() throws Exception {
        assertNull(JSON.parseObject("{\"type\":\"\"}", Model.class).type);
    }

    public void test_for_issue_null_2() throws Exception {
        assertNull(JSON.parseObject("{\"type\":\"\"}", Model.class, Feature.ErrorOnEnumNotMatch).type);
    }


    public void test_for_issue_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("\"xxx\"", Type.class, Feature.ErrorOnEnumNotMatch);
        } catch (JSONException e) {
            error = e;
        }
        assertNotNull(error);
    }

    public void test_for_issue_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"type\":\"xxx\"}", Model.class, Feature.ErrorOnEnumNotMatch);
        } catch (JSONException e) {
            error = e;
        }
        assertNotNull(error);
    }

    public enum  Type {
        Big,Small,Medium
    }

    public static class Model {
        public Type type;
    }
}
