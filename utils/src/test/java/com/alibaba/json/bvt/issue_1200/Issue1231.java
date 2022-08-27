package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 30/05/2017.
 */
public class Issue1231 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.self = model;
        model.id = 123;

        String text = JSON.toJSONString(model);
        assertEquals("{\"id\":123,\"self\":{\"$ref\":\"@\"}}", text);

        {
            Model model2 = JSON.parseObject(text, Model.class, Feature.DisableCircularReferenceDetect);
            assertNotNull(model2);
            assertNotSame(model2, model2.self);
        }

        {
            JSONObject jsonObject = JSON.parseObject(text, Feature.DisableCircularReferenceDetect);
            assertNotNull(jsonObject);

            JSONObject self = jsonObject.getJSONObject("self");

            assertNotNull(self);
            assertNotNull(self.get("$ref"));
            assertEquals("@", self.get("$ref"));
        }
    }

    public static class Model {
        public int id;
        public Model self;
    }
}
