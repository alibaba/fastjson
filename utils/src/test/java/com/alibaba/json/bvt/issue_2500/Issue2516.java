package com.alibaba.json.bvt.issue_2500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.Collection;
import java.util.List;

public class Issue2516 extends TestCase
{
    public void test_for_issue() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.toJavaObject(JSONObject.class);
        jsonObject.toJavaObject(JSON.class);

        new JSONArray().toJavaObject(JSON.class);
        new JSONArray().toJavaObject(JSONArray.class);
        new JSONArray().toJavaObject(Collection.class);
        new JSONArray().toJavaObject(List.class);
    }
}
