package com.alibaba.json.bvt.comma;

import com.alibaba.fastjson.JSONPath;
import org.junit.Assert;
import org.junit.Test;

public class TestKeyWithComma {
    private static final String short_json_str1= "{\n" +
            "   \"_aa\":{\n" +
            "      \"b_b\":{\n" +
            "           \"cc_\":\"dd\"\n" +
            "       }\n" +
            "   }\n" +
            "}";
    private static final String short_json_str1_key_with_comma = "{\n" +
            "   \",aa\":{\n" +
            "      \"b,b\":{\n" +
            "           \"cc,\":\"dd\"\n" +
            "       }\n" +
            "   }\n" +
            "}";
    private static final String json_path2 = "$._aa.b_b.cc_";
    private static final String json_path2_with_comma = "$.,aa.b,b.cc,";

    @Test
    public void test_for_issue_5() {
        String result = JSONPath.extract(short_json_str1, json_path2).toString();
        Assert.assertEquals("dd", result);
    }

    @Test
    public void test_for_issue_6() {
        Assert.assertNull(JSONPath.extract(short_json_str1, json_path2_with_comma));
    }

    @Test
    public void test_for_issue_7() {
        Assert.assertNull(JSONPath.extract(short_json_str1_key_with_comma, json_path2));
    }

    @Test
    public void test_for_issue_8() {
        String result = JSONPath.extract(short_json_str1_key_with_comma, json_path2_with_comma).toString();
        Assert.assertEquals("dd", result);
    }
}
