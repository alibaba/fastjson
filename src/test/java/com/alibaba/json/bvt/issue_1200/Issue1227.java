package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;



public class Issue1227 extends TestCase {
    public void test_for_issue() throws Exception {

        String t2 = "{\"state\":2,\"msg\":\"\ufeffmsg2222\",\"data\":[]}";

        try {
            Test model = JSON.parseObject(t2, Test.class);
            assertEquals("\uFEFFmsg2222",model.msg);

            model.msg = "\uFEFFss";
            String t3 = JSON.toJSONString(model);
            assertTrue(t3.contains(model.msg));
        } catch ( Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public static class Test {
        public int state;
        public String msg;
    }
}



