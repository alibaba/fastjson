package com.alibaba.json.bvt.writeClassName;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class StrictAutoTypeTest_0 extends TestCase {
    private int features;

    protected void setUp() throws Exception {
        this.features = JSON.DEFAULT_PARSER_FEATURE;
    }

    protected void tearDown() throws Exception {
        JSON.DEFAULT_PARSER_FEATURE = features;
    }

    public void test_0() throws Exception {
        JSON.parseObject("{\"@type\":\"com.alibaba.fastjson.JSONObject\"}", Object.class);
        JSON.parseObject("{\"@type\":\"com.alibaba.fastjson.JSONObject\"}", Object.class, Feature.SupportAutoType);
    }

    public void test_1() throws Exception {
        JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.writeClassName.StrictAutoTypeTest_0$VO\"}", Object.class, Feature.SupportAutoType);
    }

    public void test_2() throws Exception {
        {
            Exception error = null;
            try {
                JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.writeClassName.StrictAutoTypeTest_0$V1\"}", Object.class);
            } catch (JSONException ex) {
                error = ex;
            }
            assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.writeClassName.StrictAutoTypeTest_0$V1\"}", Object.class);
            } catch (JSONException ex) {
                error = ex;
            }
            assertNotNull(error);
        }
    }

    public void test_3() throws Exception {
        try {
            JSON.DEFAULT_PARSER_FEATURE |= Feature.SupportAutoType.mask;
            JSON.parseObject("{\"val\":{\"@type\":\"com.alibaba.json.bvt.writeClassName.StrictAutoTypeTest_0$V3\"}}");
        } catch (JSONException ex) {
        } finally {
            JSON.DEFAULT_PARSER_FEATURE &= ~Feature.SupportAutoType.mask;
        }
    }

    public static class VO {

    }
    public static class V1 {

    }
    public static class V2 {

    }
    public static class V3 {

    }
}
