package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue1480 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.parseObject("{35504:1,1:10,2:4,3:5,4:5,37306:98,36796:9\n" +
                "}");
    }
}
