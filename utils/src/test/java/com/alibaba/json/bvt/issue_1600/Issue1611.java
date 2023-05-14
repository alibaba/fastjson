package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

public class Issue1611 extends TestCase {
    public void test_for_issue() throws Exception {
        String pristineJson = "{\"data\":{\"lists\":[{\"Name\":\"Mark\"}]}}";
        JSONArray list = JSON.parseObject(pristineJson).getJSONObject("data").getJSONArray("lists");
        assertEquals(1, list.size());
        for (int i = 0; i < list.size(); i++) {
            JSONObject sss = list.getJSONObject(i);
            Model model = sss.toJavaObject(Model.class);
            assertEquals("Mark", model.name);
        }

    }

    public static class Model {
        private String name;

        public Model(String name) {
            this.name = name;
        }
    }
}
