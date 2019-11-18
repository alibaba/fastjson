package com.alibaba.json.bvt.issue_2400;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue2429 extends TestCase {
    public void testForIssue() {
        String str = "{\"schema\":{$ref:\"111\"},\"name\":\"ft\",\"age\":12,\"address\":\"杭州\"}";
        JSON.parseObject(str);
    }
}
