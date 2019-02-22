package com.alibaba.json.bvt.parser.autoType;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class AutoTypeTest7 extends TestCase {
    public void test_0() throws Exception {
        JSON.parseObject("{\"@type\":\"java.util.Collections$UnmodifiableMap\"}");
    }

    public void test_1() throws Exception {
        JSON.parseObject("{\"@type\":\"java.util.Collections$UnmodifiableMap\",\"id\":123}");
    }
}
