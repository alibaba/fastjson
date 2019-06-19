package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class JSONPath_12
        extends TestCase {

    public void test(){
        JSONObject schemaResult = JSON.parseObject("{\n" +
                "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
                "  \"title\": \"AE product schema\",\n" +
                "  \"description\": \"AE product schema\",\n" +
                "  \"type\": \"object\",\n" +
                "  \"required\": [\n" +
                "    \"category_attributes\"\n" +
                "  ],\n" +
                "  \"properties\": {\n" +
                "    \"category_attributes\": {\n" +
                "      \"title\": \"category attributes\",\n" +
                "      \"type\": \"object\",\n" +
                "      \"required\": [\n" +
                "        \"Brand Name\"\n" +
                "      ],\n" +
                "      \"properties\":{}\n" +
                "    }\n" +
                "  }\n" +
                "}");
        String jsonPath = "$['properties']['category_attributes']['properties']";
        String attributeName = "Brand\\. Name"; // attribute name with dot
        JSONObject attributeValue = JSON.parseObject("{\n" +
                "  \"title\": \"Brand Name\",\n" +
                "  \"type\": \"String\"\n" +
                "}");
        JSONPath.set(schemaResult, jsonPath + "['" + attributeName + "']" , attributeValue);
        assertTrue(JSONPath.contains(schemaResult, jsonPath + "['" + attributeName + "']"));
        JSONObject newAttribute = (JSONObject)JSONPath.eval(schemaResult, jsonPath);
        System.out.println(schemaResult);
        System.out.println(JSONPath.read(schemaResult.toJSONString(), jsonPath + "['" + attributeName + "']"));
        assertTrue(newAttribute.containsKey(attributeName));
    }

}
