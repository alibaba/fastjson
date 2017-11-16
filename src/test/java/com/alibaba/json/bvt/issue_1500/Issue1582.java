package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue1582 extends TestCase {
    public void test_for_issue() throws Exception {
        assertSame(Size.Big, JSON.parseObject("\"Big\"", Size.class));
        assertNull(JSON.parseObject("\"Large\"", Size.class));
    }

    public static enum Size {
        Big, Small
    }
}
