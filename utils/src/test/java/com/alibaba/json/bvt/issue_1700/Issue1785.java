package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue1785 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.parseObject("\"2006-8-9\"", java.sql.Timestamp.class);
    }
}
