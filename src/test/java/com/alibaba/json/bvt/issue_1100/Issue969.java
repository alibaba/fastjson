package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.json.bvt.issue_1200.Issue1205;
import junit.framework.TestCase;

import java.util.Date;
import java.util.List;

/**
 * Created by wenshao on 08/05/2017.
 */
public class Issue969 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new Model());
        jsonObject.put("models", jsonArray);

        List list = jsonObject.getObject("models", new TypeReference<List<Model>>(){});

        assertEquals(1, list.size());
        assertEquals(Model.class, list.get(0).getClass());
    }

    public void test_for_issue_1() throws Exception {
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new Model());
        jsonObject.put("models", jsonArray);

        List list = jsonObject.getObject("models", new TypeReference<List<Model>>(){}.getType());

        assertEquals(1, list.size());
        assertEquals(Model.class, list.get(0).getClass());
    }

    public static class Model {

    }
}
