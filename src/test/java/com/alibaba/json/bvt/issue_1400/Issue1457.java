package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue1457 extends TestCase {
    public void test_for_issue() throws Exception {
        String value = "https://github.com/alibaba/fastjson";
        String json = JSON.toJSONString(value);
        String result = JSON.parseObject(json, String.class);
        assertEquals(result, value);
    }
}
