package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 20/12/2016.
 */
public class Issue939 extends TestCase {
    public void test_for_issue_false() throws Exception {
        String jsonString = "" +
                "{" +
                "    \"age\": 25," +
                "    \"is_stop\":false/*comment*/" +
                "}";
        Model testUser = JSON.parseObject(jsonString, Model.class);
        System.out.println(testUser);
    }

    public void test_for_issue_true() throws Exception {
        String jsonString = "" +
                "{" +
                "    \"age\": 25," +
                "    \"is_stop\":true/*comment*/" +
                "}";
        Model testUser = JSON.parseObject(jsonString, Model.class);
        System.out.println(testUser);
    }

    public static class Model {
        public int age;
        public boolean is_top;
    }
}
