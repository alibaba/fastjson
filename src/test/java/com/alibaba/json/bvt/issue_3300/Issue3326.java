package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.HashMap;

public class Issue3326 extends TestCase {
    public void test_for_issue() throws Exception {
        HashMap<String, Number> map = JSON.parseObject("{\"id\":10.0}"
                , new TypeReference<HashMap<String, Number>>() {
                    }.getType()
                , 0);
        assertEquals(10.0, map.get("id"));
    }
}
