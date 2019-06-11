package com.alibaba.fastjson.validator;

import org.junit.Before;
import org.junit.Test;

public class validatorTest {
    private JSONBytes JSONBytes;
    private JSONValidator validator;

    @Before
    public void setup() {
        JSONFileByteTools JF = new JSONFileByteTools("1.json");
        byte[] bytes = JF.getJsonByte();
        JSONBytes = new JSONBytes(bytes);

        JSONFileByteTools JF1 = new JSONFileByteTools("test.json");
        byte[] bytes1 = JF1.getJsonByte();
        validator = new JSONValidator(bytes1);

    }

    @Test
    public void testlen() {
        JSONBytes.moveX(1);
        JSONBytes.TrimLeftSpace();
        System.out.println("position:" + JSONBytes.getPosition());
        System.out.print(new String(JSONBytes.getJsonBytes()));

    }

    @Test
    public void testvalidateDigit() {
        validator.validateDigit();
        System.out.println("position: " + validator.getPosition());
        System.out.print(new String(validator.getJsonBytes()));
    }

    @Test
    public void testvalidateNumber() {
        validator.validateNumber();
        System.out.println("position: " + validator.getPosition());
        System.out.print(new String(validator.getJsonBytes()));
    }

    @Test
    public void testvalidateEscTest() {
        validator.validateEsc();;
        System.out.println("position: " + validator.getPosition());
        System.out.print(new String(validator.getJsonBytes()));
    }

    @Test
    public void testLittle() {

    }
}
























