package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Issue3656 {
    static class issue3656_Type {
        public List<String> enums = new ArrayList<String>();
        public String typeBuilderName = "";
        public String type = "";
    }


    static class issue3656_Metadata {
        public String serviceType;
        public Map<String, issue3656_Type> types = new HashMap<String, issue3656_Type>();
    }

    @Test(expected = JSONException.class)
    public void test_issue_1() {
        String jsonStr = "{\n" +
                "    \"serviceType\":\"dubbo\",\n" +
                "    \"types\":[\n" +
                "        {\n" +
                "            \"enums\":[\n" +
                "            ],\n" +
                "            \"typeBuilderName\":\"DefaultTypeBuilder\",\n" +
                "            \"type\":\"int\",\n" +
                "            \"items\":[\n" +
                "            ],\n" +
                "            \"properties\":{\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        JSON.parseObject(jsonStr, issue3656_Metadata.class);
    }
}
