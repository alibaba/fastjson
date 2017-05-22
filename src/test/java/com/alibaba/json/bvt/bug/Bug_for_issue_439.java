package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_439 extends TestCase {
    public void test_for_issue() throws Exception {
       JSON.parseObject("{/*aa*/}");
    }
    
}
