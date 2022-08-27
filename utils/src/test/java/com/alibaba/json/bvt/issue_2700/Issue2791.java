package com.alibaba.json.bvt.issue_2700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class Issue2791 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject jsonObject = JSON.parseObject("{\"dependencies\":[{\"values\":[{\"name\":\"Demo\"}]}]}");
        JSONPath.remove(jsonObject, "$.dependencies.values[?(@.name=='Demo')]");
        assertEquals("{\"dependencies\":[{\"values\":[]}]}", jsonObject.toString());
    }

    public void test_for_issue1() throws Exception {
        JSONObject jsonObject = JSON.parseObject("{\"dependencies\":[{\"values\":{\"name\":\"Demo\"}}]}");
        JSONPath.remove(jsonObject, "$.dependencies.values[?(@.name=='Demo')]");
        assertEquals("{\"dependencies\":[]}", jsonObject.toString());
    }

    public void test_for_issue2() throws Exception {
        JSONObject jsonObject = JSON.parseObject("{\"values\":[{\"name\":\"Demo\"}]}");
        JSONPath.remove(jsonObject, "$.values[?(@.name=='Demo')]");
        assertEquals("{\"values\":[]}", jsonObject.toString());
    }


    public void test_for_issue3() throws Exception {
        JSONObject jsonObject = JSON.parseObject("{\"values\":{\"name\":\"Demo\"}}");
        assertTrue(JSONPath.remove(jsonObject, "$.values[?(@.name=='Demo')]"));
        assertEquals("{}", jsonObject.toString());
    }
}
