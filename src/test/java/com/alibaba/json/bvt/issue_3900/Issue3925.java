package com.alibaba.json.bvt.issue_3900;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import lombok.Value;

import java.math.BigDecimal;

/**
 * Created by eliash0913 on 2021-11-07
 */

public class Issue3925 extends TestCase {
    public void test_for_issue() throws Exception {
        final String jsonString1 = "{\n" +
                "        \"id\": 1L,\n" +
                "        \"value\": 123,\n" +
                "}";

        final String jsonString2 = "{\n" +
                "        \"id\": 1,\n" +
                "        \"value\": 12345,\n" +
                "}";

        final String jsonString3 = "{\n" +
                "        \"id\": 1,\n" +
                "        \"value\": 123.45,\n" +
                "}";

        TestClass1 tc1_1 = JSON.parseObject(jsonString1).toJavaObject(TestClass1.class);
        TestClass1 tc1_2 = JSON.parseObject(jsonString2).toJavaObject(TestClass1.class);
        TestClass1 tc1_3 = JSON.parseObject(jsonString3).toJavaObject(TestClass1.class);
        assertEquals(1,tc1_1.id);
        assertEquals(1,tc1_2.id);
        assertEquals(1,tc1_3.id);
        assertEquals(123,tc1_1.value);
        assertEquals(12345,tc1_2.value);
        assertEquals(123,tc1_3.value);

        TestClass2 tc2_1 = JSON.parseObject(jsonString1).toJavaObject(TestClass2.class);
        TestClass2 tc2_2 = JSON.parseObject(jsonString2).toJavaObject(TestClass2.class);
        TestClass2 tc2_3 = JSON.parseObject(jsonString3).toJavaObject(TestClass2.class);
        assertEquals(1,tc2_1.id);
        assertEquals(1,tc2_2.id);
        assertEquals(1,tc2_3.id);
        assertEquals(Long.valueOf(123),tc2_1.value);
        assertEquals(Long.valueOf(12345),tc2_2.value);
        assertEquals(Long.valueOf(123),tc2_3.value);

        TestClass3 tc3_1 = JSON.parseObject(jsonString1).toJavaObject(TestClass3.class);
        TestClass3 tc3_2 = JSON.parseObject(jsonString2).toJavaObject(TestClass3.class);
        TestClass3 tc3_3 = JSON.parseObject(jsonString3).toJavaObject(TestClass3.class);
        assertEquals(1,tc3_1.id);
        assertEquals(1,tc3_2.id);
        assertEquals(1,tc3_3.id);
        assertEquals(123.0,tc3_1.value);
        assertEquals(12345.0,tc3_2.value);
        assertEquals(123.45,tc3_3.value);

        TestClass4 tc4_1 = JSON.parseObject(jsonString1).toJavaObject(TestClass4.class);
        TestClass4 tc4_2 = JSON.parseObject(jsonString2).toJavaObject(TestClass4.class);
        TestClass4 tc4_3 = JSON.parseObject(jsonString3).toJavaObject(TestClass4.class);
        assertEquals(1,tc4_1.id);
        assertEquals(1,tc4_2.id);
        assertEquals(1,tc4_3.id);
        assertEquals((float) 123,tc4_1.value);
        assertEquals((float) 12345,tc4_2.value);
        assertEquals((float) 123.45,tc4_3.value);
    }

    @Value
    public static class TestClass1 {
        int id;
        int value;
    }

    @Value
    public static class TestClass2 {
        int id;
        Long value;
    }

    @Value
    public static class TestClass3 {
        int id;
        double value;
    }

    @Value
    public static class TestClass4 {
        int id;
        float value;
    }
}

