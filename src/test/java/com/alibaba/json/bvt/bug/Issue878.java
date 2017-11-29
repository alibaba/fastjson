package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

/**
 * Created by wenshao on 2016/11/10.
 */
public class Issue878 extends TestCase {
    public void test_for_issue() throws Exception {
        String jsonVal0 = "{\"id\":5001,\"name\":\"Jobs\"}";
        String jsonVal1 = "{\"id\":5382,\"user\":\"Mary\"}";
        String jsonVal2 = "{\"id\":2341,\"person\":\"Bob\"}";

        Model obj0 = JSON.parseObject(jsonVal0, Model.class);
        assertEquals(5001, obj0.id);
        assertEquals("Jobs", obj0.name);

        Model obj1 = JSON.parseObject(jsonVal1, Model.class);
        assertEquals(5382, obj1.id);
        assertEquals("Mary", obj1.name);

        Model obj2 = JSON.parseObject(jsonVal2, Model.class);
        assertEquals(2341, obj2.id);
        assertEquals("Bob", obj2.name);
    }

    public static class Model {
        public int id;

        @JSONField(alternateNames = {"user", "person"})
        public String name;
    }
}
