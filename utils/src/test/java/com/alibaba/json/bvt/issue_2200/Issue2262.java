package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class Issue2262 extends TestCase {
    public void test_for_issue() throws Exception {
        Model m = new Model();
        m.javaVersion = "1.6";

        String json = JSON.toJSONString(m);
        assertEquals("{\"java.version\":\"1.6\"}", json);

        Model m2 = JSON.parseObject(json, Model.class);
        assertNotNull(m2);
        assertEquals(m.javaVersion, m2.javaVersion);
    }

    public static class Model {
        @JSONField(name = "java.version")
        public String javaVersion;
    }
}
