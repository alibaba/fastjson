package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue3282 extends TestCase {
    public void test_for_issue() {
        Demo demo = JSON.parseObject("{'date':'2020-01-01 00:00:00 000'}", Demo.class);
        assertNotNull(demo.date);
    }

    public static class Demo {
        public java.util.Date date;
    }
}