package com.alibaba.fastjson.jsonpath.issue3493;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author wangzn
 * @since 2020/10/27 10:27
 */
public class TestIssue3493 {

    @Test
    public void testIssue3493(){
        String json = "{\n" +
                "\"result\": [\n" +
                "{\n" +
                "\"puid\": \"21025318\"\n" +
                "},\n" +
                "{\n" +
                "\"puid\": \"21482682\"\n" +
                "},\n" +
                "{\n" +
                "\"puid\": \"21025345\"\n" +
                "}\n" +
                "],\n" +
                "\"state\": 0\n" +
                "}";
        Object list = JSONPath.extract(json, "$.result[0,2].puid");
        JSONArray jsonArray = JSON.parseArray(list.toString());
        Assert.assertEquals(jsonArray.size(), 2);
        Assert.assertEquals(jsonArray.get(0), "21025318");
        Assert.assertEquals(jsonArray.get(1), "21025345");
    }
}

