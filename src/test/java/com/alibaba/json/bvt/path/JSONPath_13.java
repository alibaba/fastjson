package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class JSONPath_13
        extends TestCase {

    public void test_0() {
        JSONObject root = new JSONObject();
        root.put("company", new JSONObject());
        root.getJSONObject("company").put("id", 123);
        root.getJSONObject("company").put("name", "jobs");

        JSONPath.remove(root, "$..id");

        assertEquals("{\"company\":{\"name\":\"jobs\"}}", root.toJSONString());
    }

    public void test_1() {
        Root root = new Root();
        root.company = new Company();
        root.company.id = 123;
        root.company.name = "jobs";

        JSONPath.remove(root, "$..id");

        assertEquals("{\"company\":{\"name\":\"jobs\"}}", JSON.toJSONString(root));
    }

    public static class Root {
        public Company company;
    }

    public static class Company {
        public Integer id;
        public String name;
    }
}
