package com.alibaba.json.bvt.issue_3700;

import java.util.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.junit.Assert;
import org.junit.Test;

public class Issue3719 {
    private static final String stringBeforeDecode = "ewogICAgICJzdGFvbnMiOiBbCiAgICAgewogICAgICIkcmVmIjogIi4gICJocmVmIi97VA==";
    private static final String stringAfterDecode = "{\n" +
            "     \"staons\": [\n" +
            "     {\n" +
            "     \"$ref\": \".  \"href\"/{T";
    private static final String stringAfterDecode2 = "{\n" +
            "     \"staons\": [\n" +
            "     {\n" +
            "     \"$ref\": \".  \"href\"/{";
    private static final String stringAfterDecode3 = "{\n" +
            "     \"staons\": [\n" +
            "     {\n" +
            "     \"$ref\": \".  \"href\"/";
    private static final String stringAfterDecode4 = "{\n" +
            "     \"staons\": [\n" +
            "     {\n" +
            "     \"$ref\": \".  \"href\"";
    private static final String stringAfterDecode5 = "{\n" +
            "     \"staons\": [\n" +
            "     {\n" +
            "     \"$ref\": \".  \"href";
    private static final String stringAfterDecode6 = "{\n" +
            "     \"staons\": [\n" +
            "     {\n" +
            "     \"$ref\": \".  \"";
    private static final String stringAfterDecode7 = "{\n" +
            "     \"staons\": [\n" +
            "     {\n" +
            "     \"$ref\": \". \"";
    private static final String stringAfterDecode8 = "{\n" +
            "     \"staons\": [\n" +
            "     {\n" +
            "     \"$ref\": \".\"";
    public static String btoa(String base64) {
        return new String(Base64.getDecoder().decode(base64));
    }

    @Test
    public void test_str_equal() {
        Assert.assertEquals(stringAfterDecode, btoa(stringBeforeDecode));
    }

    @Test(expected = JSONException.class)
    public void test_for_issue_wrong_case1() {
        JSON.parse(stringAfterDecode);
    }

    @Test(expected = JSONException.class)
    public void test_for_issue_wrong_case2() {
        JSON.parse(stringAfterDecode2);
    }

    @Test(expected = JSONException.class)
    public void test_for_issue_wrong_case3() {
        JSON.parse(stringAfterDecode3);
    }

    @Test(expected = JSONException.class)
    public void test_for_issue_wrong_case4() {
        JSON.parse(stringAfterDecode4);
    }

    @Test(expected = JSONException.class)
    public void test_for_issue_wrong_case5() {
        JSON.parse(stringAfterDecode5);
    }

    @Test(expected = JSONException.class)
    public void test_for_issue_wrong_case6() {
        JSON.parse(stringAfterDecode6);
    }
    @Test(expected = JSONException.class)
    public void test_for_issue_wrong_case7() {
        JSON.parse(stringAfterDecode7);
    }
    @Test(expected = JSONException.class)
    public void test_for_issue_wrong_case8() {
        JSON.parse(stringAfterDecode8);
    }
}