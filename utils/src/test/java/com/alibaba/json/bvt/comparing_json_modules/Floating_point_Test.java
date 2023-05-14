package com.alibaba.json.bvt.comparing_json_modules;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.DoubleSerializer;
import junit.framework.TestCase;

/**
 * Created by wenshao on 24/03/2017.
 */
public class Floating_point_Test extends TestCase {
    public void test_2_1() throws Exception {
        assertEquals("0.0", JSON.toJSONString(0.0));
    }

    public void test_2_2() throws Exception {
        assertEquals("-0.0", JSON.toJSONString(-0.0F));
    }

    public void test_2_3() throws Exception {
        assertEquals("1.0", JSON.toJSONString(1.0));
    }

    public void test_2_4() throws Exception {
        assertEquals("0.1", JSON.toJSONString(0.1));
    }

    public void test_2_5() throws Exception {
        assertEquals("3.141592653589793", JSON.toJSONString(Math.PI));
    }

    public void test_2_6() throws Exception {
        double doubeValue = Math.pow(Math.PI, 100);
        assertEquals("5.187848314319592E49", JSON.toJSONString(doubeValue));
    }

    public void test_2_7() throws Exception {
        double doubeValue = Math.pow(Math.PI, -100);

        String json = JSON.toJSONString(doubeValue);
        // 1.9275814160560204E-50
        // 1.9275814160560206E-50
        assertTrue(json.equals("1.9275814160560206E-50")
                || json.equals("1.9275814160560204E-50") // raspberry pi
        );
    }
}
