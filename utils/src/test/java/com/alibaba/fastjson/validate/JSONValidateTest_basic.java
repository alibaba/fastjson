package com.alibaba.fastjson.validate;

import com.alibaba.fastjson.JSONValidator;
import junit.framework.TestCase;

public class JSONValidateTest_basic extends TestCase
{
    public void test_for_bastic_true() throws Exception {
        assertTrue(JSONValidator.from("{\"id\":true}").validate());
        assertTrue(JSONValidator.from("[true]").validate());
        assertTrue(JSONValidator.from("true").validate());
    }

    public void test_for_bastic_false() throws Exception {
        assertTrue(JSONValidator.from("{\"id\":false}").validate());
        assertTrue(JSONValidator.from("[false]").validate());
        assertTrue(JSONValidator.from("false").validate());
    }

    public void test_for_bastic_null() throws Exception {
        assertTrue(JSONValidator.from("{\"id\":null}").validate());
        assertTrue(JSONValidator.from("[null]").validate());
        assertTrue(JSONValidator.from("null").validate());


    }

    public void test_long2ip() {
        long a = 1677734491111L;
        long b = 2697245671L;
        long longVal = a;

        int intVal = -1597721625;
        long unsignedIntVal = intVal & 0xFFFFFFFFL;
        boolean negative = (longVal & 0xFFFFFFFF00000000L) != 0;



        System.out.println(intVal & 0xFFFFFFFFL);

        System.out.println((int) b);

        byte[] bytes0 = new byte[8];
        byte[] bytes1 = new byte[8];
        byte[] bytes2 = new byte[8];

        putLong(bytes0, 0, a);
        putLong(bytes1, 0, b);
        putLong(bytes2, 0, 0xFFFFFFFF00000000L);
        System.out.println("");

    }

    static void putLong(byte[] b, int off, long val) {
        b[off + 7] = (byte) (val       );
        b[off + 6] = (byte) (val >>>  8);
        b[off + 5] = (byte) (val >>> 16);
        b[off + 4] = (byte) (val >>> 24);
        b[off + 3] = (byte) (val >>> 32);
        b[off + 2] = (byte) (val >>> 40);
        b[off + 1] = (byte) (val >>> 48);
        b[off    ] = (byte) (val >>> 56);
    }
}
