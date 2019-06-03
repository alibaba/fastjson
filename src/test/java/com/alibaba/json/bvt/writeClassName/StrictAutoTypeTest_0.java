package com.alibaba.json.bvt.writeClassName;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

public class StrictAutoTypeTest_0 extends TestCase {
    private ParserConfig config = new ParserConfig();

    public void test_0() throws Exception {
        JSON.parseObject("{\"@type\":\"com.alibaba.fastjson.JSONObject\"}", Object.class, config);
        JSON.parseObject("{\"@type\":\"com.alibaba.fastjson.JSONObject\"}", Object.class, config, Feature.SupportAutoType);
    }

    public void test_1() throws Exception {
        JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.writeClassName.StrictAutoTypeTest_0$VO\"}", Object.class, config, Feature.SupportAutoType);
    }

    public void test_2() throws Exception {
        {
            Exception error = null;
            try {
                JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.writeClassName.StrictAutoTypeTest_0$V1\"}", Object.class, config);
            } catch (JSONException ex) {
                error = ex;
            }
            assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.writeClassName.StrictAutoTypeTest_0$V1\"}", Object.class, config);
            } catch (JSONException ex) {
                error = ex;
            }
            assertNotNull(error);
        }
    }

    public void test_3() throws Exception {
        int features = JSON.DEFAULT_PARSER_FEATURE | Feature.SupportAutoType.mask;
        JSON.parse("{\"val\":{\"@type\":\"com.alibaba.json.bvt.writeClassName.StrictAutoTypeTest_0$V3\"}}", config, features);
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
