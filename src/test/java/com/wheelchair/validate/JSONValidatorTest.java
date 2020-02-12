package com.wheelchair.validate;

import com.alibaba.fastjson.JSONValidator;
import org.junit.Test;


import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

public class JSONValidatorTest {

    @Test
    public void validate_test1() throws Throwable {
        assertTrue(JSONValidator.from("{\"string\":\"a\",\"nums\":[0,-1,10,0.123,1e5,-1e+6,+0.1e-7],\"object\":{\"empty\":{},\"list\":[]},\"list\":[\"object\",{\"true\":true,\"false\":false,\"null\":null}]}").validate());
    }
    @Test
    public void validate_test2() throws Throwable {
        assertTrue(JSONValidator.from("{noQuotationMarksError}").validate());
    }
    @Test
    public void validate_test3() throws Throwable {
        assertTrue(JSONValidator.from("{\"colonError\"}").validate());

    }
    @Test
    public void validate_test4() throws Throwable {
        assertTrue(JSONValidator.from("{\"square_brackets_error\": [1}").validate());

    }
    @Test
    public void validate_test5() throws Throwable {
        assertTrue(JSONValidator.from("{\"num_err1\":+a}").validate());
    }
    @Test
    public void validate_test6() throws Throwable {
        assertTrue(JSONValidator.from("{\"num_err1\":1ea}").validate());
    }
    @Test
    public void validate_test7() throws Throwable {
        assertTrue(JSONValidator.from("{\"num_err1\":trua}").validate());
    }
}