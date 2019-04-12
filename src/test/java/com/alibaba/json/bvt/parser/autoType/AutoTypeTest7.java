package com.alibaba.json.bvt.parser.autoType;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.springframework.cache.support.NullValue;

public class AutoTypeTest7 extends TestCase {
    public void test_0() throws Exception {
        JSON.parseObject("{\"@type\":\"java.util.Collections$UnmodifiableMap\"}");
    }

    public void test_1() throws Exception {
        JSON.parseObject("{\"@type\":\"java.util.Collections$UnmodifiableMap\",\"id\":123}");
    }

    public void test_2() throws Exception {
        NullValue value = (NullValue) JSON.parseObject("{\"@type\":\"org.springframework.cache.support.NullValue\"}", Object.class);
        assertNotNull(value);
    }

}
