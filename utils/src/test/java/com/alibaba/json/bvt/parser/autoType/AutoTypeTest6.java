package com.alibaba.json.bvt.parser.autoType;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class AutoTypeTest6 extends TestCase {
    public void test_0() throws Exception {
        JSON.parseObject("{\"@type\":\"com.alibaba.fastjson.util.AntiCollisionHashMap\"}");
    }
}
