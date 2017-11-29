package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class Issue1582 extends TestCase {
    public void test_for_issue() throws Exception {
        assertSame(Size.Big, JSON.parseObject("\"Big\"", Size.class));
        assertSame(Size.Big, JSON.parseObject("\"big\"", Size.class));
        assertNull(JSON.parseObject("\"Large\"", Size.class));
        assertSame(Size.LL, JSON.parseObject("\"L3\"", Size.class));

        assertSame(Size.Small, JSON.parseObject("\"Little\"", Size.class));
    }

    public static enum Size {
        Big,

        @JSONField(alternateNames = "Little")
        Small,

        @JSONField(name = "L3")
        LL
    }
}
