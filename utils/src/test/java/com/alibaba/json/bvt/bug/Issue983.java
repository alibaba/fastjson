package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Created by wenshao on 10/01/2017.
 */
public class Issue983 extends TestCase {
    public void test_for_issue() throws Exception {
        Map.Entry entry = JSON.parseObject("{\"name\":\"foo\"}", Map.Entry.class);
        assertEquals("name", entry.getKey());
        assertEquals("foo", entry.getValue());
    }
}
