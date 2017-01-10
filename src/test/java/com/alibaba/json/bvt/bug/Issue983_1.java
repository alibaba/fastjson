package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Created by wenshao on 10/01/2017.
 */
public class Issue983_1 extends TestCase {
    public void test_for_issue() throws Exception {
        Map.Entry entry = new AbstractMap.SimpleEntry("name", "foo");
        String text = JSON.toJSONString(entry);
        assertEquals("{\"name\":\"foo\"}", text);
    }

    public void test_for_issue_int() throws Exception {
        Map.Entry entry = new AbstractMap.SimpleEntry("name", 123);
        String text = JSON.toJSONString(entry);
        assertEquals("{\"name\":123}", text);
    }

    public void test_for_issue_int_int() throws Exception {
        Map.Entry entry = new AbstractMap.SimpleEntry(123, 234);
        String text = JSON.toJSONString(entry);
        assertEquals("{123:234}", text);
    }
}
