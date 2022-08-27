package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.List;

public class Issue2358 extends TestCase {

    public void test_for_issue() throws Exception {
        String str = "[{\n" +
                "  \"test1\":\"1\",\n" +
                "  \"test2\":\"2\"\n" +
                "},\n" +
                " {\n" +
                "   \"test1\":\"1\",\n" +
                "   \"test2\":\"2\"\n" +
                " }]";

        Exception error = null;
        try {
            List<TestJson2> testJsons = JSONObject.parseArray(str, TestJson2.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertEquals("can't create non-static inner class instance.", error.getMessage());
    }

    class TestJson {

        private String test1;
        private String test2;

        public String getTest1() {
            return test1;
        }

        public void setTest1(String test1) {
            this.test1 = test1;
        }

        public String getTest2() {
            return test2;
        }

        public void setTest2(String test2) {
            this.test2 = test2;
        }


    }

    class TestJson2 {
        private String test1;
        private String test2;

        public String getTest1() {
            return test1;
        }

        public void setTest1(String test1) {
            this.test1 = test1;
        }

        public String getTest2() {
            return test2;
        }

        public void setTest2(String test2) {
            this.test2 = test2;
        }
    }
}
