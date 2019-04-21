package com.alibaba.json.bvt.parser.number;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.math.BigDecimal;

public class NumberValueTest_cast extends TestCase {
    public void test_0() throws Exception {
        JSONObject object = new JSONObject();
        object.put("val", new BigDecimal("23.4"));

        assertEquals(23, object.getByteValue("val"));
        assertEquals(23, object.getShortValue("val"));
        assertEquals(23, object.getIntValue("val"));
        assertEquals(23, object.getLongValue("val"));
    }
}
