package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import org.junit.Test;

public class Issue3689 {

    @Test(expected = JSONException.class)
    public void test_without_type_0() {
        JSONArray.parseArray("dfdfdf");
    }

    @Test(expected = JSONException.class)
    public void test_without_type_1() {
        JSONArray.parseArray("/dfdfdf");
    }

    @Test(expected = JSONException.class)
    public void test_without_type_2() {
        JSONArray.parseArray("//dfdfdf");
    }

    @Test(expected = JSONException.class)
    public void test_without_type_3() {
        JSONArray.parseArray("///dfdfdf");
    }

    @Test(expected = JSONException.class)
    public void test_without_type_4() {
        JSONArray.parseArray("////dfdfdf");
    }

    @Test(expected = JSONException.class)
    public void test_without_type_5() {
        JSONArray.parseArray("/////dfdfdf");
    }

    @Test(expected = JSONException.class)
    public void test_without_type_6() {
        JSONArray.parseArray("//////dfdfdf");
    }

    @Test(expected = JSONException.class)
    public void test_with_type_0() {
        JSONArray.parseArray("dfdfdf", String.class);
    }

    @Test(expected = JSONException.class)
    public void test_with_type_1() {
        JSONArray.parseArray("/dfdfdf", String.class);
    }

    @Test(expected = JSONException.class)
    public void test_with_type_2() {
        JSONArray.parseArray("//dfdfdf", String.class);

    }

    @Test(expected = JSONException.class)
    public void test_with_type_3() {
        JSONArray.parseArray("///dfdfdf", String.class);

    }

    @Test(expected = JSONException.class)
    public void test_with_type_4() {
        JSONArray.parseArray("////dfdfdf", String.class);

    }

    @Test(expected = JSONException.class)
    public void test_with_type_5() {
        JSONArray.parseArray("/////dfdfdf", String.class);

    }

    @Test(expected = JSONException.class)
    public void test_with_type_6() {
        JSONArray.parseArray("//////dfdfdf", String.class);
    }

    @Test
    public void test_2() {
        JSONArray.parseArray("[\"////dfdfdf\"]"); //不会抛异常
        JSONArray objects = JSONArray.parseArray("[\"dfdfdf\"]");//不会抛异常
        System.out.println(JSONArray.parseArray("[\"////dfdfdf\"]"));
        System.out.println(JSONArray.parseArray("[\"dfdfdf\"]"));
    }
}
