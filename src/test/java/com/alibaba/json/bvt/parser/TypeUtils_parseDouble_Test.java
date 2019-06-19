package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.util.TypeUtils;
import junit.framework.TestCase;

import java.util.Random;

public class TypeUtils_parseDouble_Test extends TestCase {
    public void test_0() throws Exception {
        Random r = new Random();

        for (int i = 0; i < 1000 * 1000; ++i) {
            String str = Float.toString(r.nextFloat());
            assertEquals(Double.parseDouble(str), TypeUtils.parseDouble(str));
        }
    }

    public void test_0_d() throws Exception {
        Random r = new Random();

        for (int i = 0; i < 1000 * 1000; ++i) {
            String str = Double.toString(r.nextDouble());
            assertEquals(Double.parseDouble(str), TypeUtils.parseDouble(str));
        }
    }


    public void test_1() throws Exception {
        Random r = new Random();

        for (int i = 0; i < 1000 * 1000; ++i) {
            String str = Integer.toString(r.nextInt());
            assertEquals(Double.parseDouble(str), TypeUtils.parseDouble(str));
        }
    }

    public void test_2() throws Exception {
        Random r = new Random();

        for (int i = 0; i < 1000 * 1000; ++i) {
            String str = Integer.toString(r.nextInt(1000000000));
            assertEquals(Double.parseDouble(str), TypeUtils.parseDouble(str));
        }
    }

    public void test_3() throws Exception {
        Random r = new Random();

        for (int i = 0; i < 1000 * 1000; ++i) {
            String str = Long.toString(r.nextLong());
            assertEquals(Double.parseDouble(str), TypeUtils.parseDouble(str));
        }
    }

    public void test_4() throws Exception {
        String[] array = new String[] {
                "0.34856254",
                "1",
                "12",
                "123",
                "1234",
                "12345",
                "123456",
                "1234567",
                "12345678",
                "123456789",
                "1234567890",
                ".1"
                ,"1.1"
                ,"12.1"
                , "123.1"
                , "1234.1"
                , "12345.1"
                , "123456.1"
                , "1234567.1"
                , "12345678.1"
                , "0.1"
                , "0.12"
                , "0.123"
                , "0.1234"
                , "0.12345"
                , "0.123456"
                , "0.1234567"
                , "0.12345678"
                , "0.123456789"
                , "0.1234567891"
                , "0.12345678901"
                , "0.123456789012"
        };

        for (String str : array) {
            assertEquals(Double.parseDouble(str), TypeUtils.parseDouble(str));
        }
    }
}
