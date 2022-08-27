package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.List;
import java.util.Map;

/**
 * Created by wenshao on 05/05/2017.
 */
public class Issue1177_3 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "[{\"x\":\"y\"},{\"x\":\"y\"}]";
        List<Model> jsonObject = JSONObject.parseObject(text, new TypeReference<List<Model>>(){});
        System.out.println(JSON.toJSONString(jsonObject));
        String jsonpath = "$..x";
        String value="y2";
        JSONPath.set(jsonObject, jsonpath, value);
        assertEquals("[{\"x\":\"y2\"},{\"x\":\"y2\"}]", JSON.toJSONString(jsonObject));

    }

    public static class Model {
        public String x;
    }
}
