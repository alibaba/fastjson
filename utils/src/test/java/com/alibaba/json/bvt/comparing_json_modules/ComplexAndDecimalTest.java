package com.alibaba.json.bvt.comparing_json_modules;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import junit.framework.TestCase;

import java.math.BigDecimal;

/**
 * Created by wenshao on 24/03/2017.
 */
public class ComplexAndDecimalTest extends TestCase {
    public void test_3_1() throws Exception {
        assertEquals("5", JSON.toJSONString(5L));
    }

    public void test_3_2() throws Exception {
        assertEquals("5.5", JSON.toJSONString(new BigDecimal("5.5")));
    }

    public void test_3_4() throws Exception {
        assertEquals("5", JSON.toJSONString(new BigDecimal("5")));
    }

    public void test_3_5() throws Exception {
        assertEquals("0.1", JSON.toJSONString(new BigDecimal("0.1")));
    }

    public void test_3_6() throws Exception {
        assertEquals("0.1", JSON.toJSONString(new BigDecimal("0.1")));
    }

    public void test_3_7() throws Exception {
        assertEquals("3.14159265358979323846264338327950288419716939937510", JSON.toJSONString(new BigDecimal("3.14159265358979323846264338327950288419716939937510")));
    }
}
