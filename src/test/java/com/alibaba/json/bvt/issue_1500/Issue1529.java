package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue1529 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\"isId\":false,\"Id\":138042533,\"name\":\"example\",\"height\":172}";
        JSON.parseObject(text, Person.class);
    }

    public static class Person {
        public int Id;
        public String name;
        public double height;
    }
}
