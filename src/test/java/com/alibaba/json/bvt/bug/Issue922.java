package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 20/12/2016.
 */
public class Issue922 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "[1,2,3]";
        JSONArray array = JSON.parseArray(text);
        List<Long> list = array.toJavaList(Long.class);
        assertEquals(1L, list.get(0).longValue());
        assertEquals(2L, list.get(1).longValue());
        assertEquals(3L, list.get(2).longValue());
    }
}
