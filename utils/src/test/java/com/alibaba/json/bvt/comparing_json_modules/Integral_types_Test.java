package com.alibaba.json.bvt.comparing_json_modules;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 24/03/2017.
 */
public class Integral_types_Test extends TestCase {
    public void test_1_1() throws Exception {
        assertEquals("0", JSON.toJSONString(0));
    }

    public void test_1_2() throws Exception {
        assertEquals("1", JSON.toJSONString(1));
    }

    public void test_1_3() throws Exception {
        assertEquals("123456789", JSON.toJSONString(123456789));
    }

    public void test_1_4() throws Exception {
        assertEquals("-123456789", JSON.toJSONString(-123456789));
    }

    public void test_1_5() throws Exception {
        assertEquals("2147483647", JSON.toJSONString(Integer.MAX_VALUE));
    }

    public void test_1_6() throws Exception {
        String text = "-9999999999999999999943";
        assertEquals(text, JSON.toJSONString(JSON.parse(text)));
    }
}
