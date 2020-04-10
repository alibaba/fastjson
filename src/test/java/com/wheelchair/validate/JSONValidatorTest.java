package com.wheelchair.validate;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONValidator;
import org.junit.Test;


import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

public class JSONValidatorTest {

    @Test
    public void validate_test_accurate() throws Throwable {
        boolean isValidate = JSONValidator.from("{\"string\":\"a\",\"nums\":[0,-1,10,0.123,1e5,-1e+6,0.1e-7],\"object\":{\"empty\":{},\"list\":[]},\"list\":[\"object\",{\"true\":true,\"false\":false,\"null\":null}]}").validate();
        assertTrue(isValidate);
    }

    @Test
    public void validate_test_quotation() throws Throwable {
        boolean isValidate = JSONValidator.from("{noQuotationMarksError}").validate();
        assertFalse(isValidate);
    }

    @Test
    public void validate_test_colon() throws Throwable {
        boolean isValidate = JSONValidator.from("{\"colonError\"}").validate();
        assertFalse(isValidate);

    }

    @Test
    public void validate_test_bracket() throws Throwable {
        boolean isValidate = JSONValidator.from("[1}").validate();
        assertFalse(isValidate);
    }

    @Test
    public void validate_test_num1() throws Throwable {
        boolean isValidate = JSONValidator.from("-a").validate();
        assertFalse(isValidate);
    }

    @Test
    public void validate_test_num2() throws Throwable {
        boolean isValidate = JSONValidator.from("1.a1").validate();
        assertFalse(isValidate);
    }

    @Test
    public void validate_test_num3() throws Throwable {
        boolean isValidate = JSONValidator.from("1.e1").validate();
        assertFalse(isValidate);
    }

    @Test
    public void validate_test_num4() throws Throwable {
        boolean isValidate = JSONValidator.from("+1").validate();
        assertFalse(isValidate);
    }

    @Test
    public void validate_test_num5() throws Throwable {
        boolean isValidate = JSONValidator.from("1ea").validate();
        assertFalse(isValidate);
    }

    @Test
    public void validate_test_tfn() throws Throwable {
        boolean isValidate = JSONValidator.from("trua").validate();
        assertFalse(isValidate);
    }
}