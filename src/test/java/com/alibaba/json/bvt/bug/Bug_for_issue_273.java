package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_273 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.parseObject("{\"value\":\"\0x16\0x26\"}");
    }
    
    public static class VO {
        public String value;
    }
}
