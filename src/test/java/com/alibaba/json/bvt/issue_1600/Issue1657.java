package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.HashMap;

public class Issue1657 extends TestCase {
    public void test_for_issue() throws Exception {
        HashMap map = JSON.parseObject("\"\"", HashMap.class);
        assertEquals(0, map.size());
    }
}
