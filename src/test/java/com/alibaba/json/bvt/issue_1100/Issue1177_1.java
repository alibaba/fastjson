package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

/**
 * Created by wenshao on 05/05/2017.
 */
public class Issue1177_1 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\"a\":{\"x\":\"y\"},\"b\":{\"x\":\"y\"}}";
        JSONObject jsonObject = JSONObject.parseObject(text);
        System.out.println(jsonObject);
        String jsonpath = "$..x";
        String value="y2";
        JSONPath.set(jsonObject, jsonpath, value);
        assertEquals("{\"a\":{\"x\":\"y2\"},\"b\":{\"x\":\"y2\"}}", jsonObject.toString());

    }
}
