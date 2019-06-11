package com.alibaba.fastjson.validator;

import org.junit.Before;
import org.junit.Test;

public class validatorTest {
    private JSONBytes JSONBytes;


    @Before
    public void setup() {
        JSONFileByteTools JF = new JSONFileByteTools("1.json");
        byte[] bytes = JF.getJsonByte();
        JSONBytes = new JSONBytes(bytes);
    }

    @Test
    public void testlen() {
        JSONBytes.moveX(1);
        JSONBytes.TrimLeftSpace();
        System.out.println("position:" + JSONBytes.getPosition());
        System.out.print(new String(JSONBytes.getJsonBytes()));

    }
}

