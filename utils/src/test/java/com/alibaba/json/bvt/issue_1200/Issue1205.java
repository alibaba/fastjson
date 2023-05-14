package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 11/06/2017.
 */
public class Issue1205 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONArray array = new JSONArray();
        array.add(new JSONObject());

        List<Model> list = array.toJavaObject(new TypeReference<List<Model>>(){});
        assertEquals(1, list.size());
        assertEquals(Model.class, list.get(0).getClass());
    }

    public static class Model {

    }
}
