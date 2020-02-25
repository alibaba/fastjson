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
        boolean thrown = false;
        try {
            JSONValidator.from("{\"string\":\"a\",\"nums\":[0,-1,10,0.123,1e5,-1e+6,0.1e-7],\"object\":{\"empty\":{},\"list\":[]},\"list\":[\"object\",{\"true\":true,\"false\":false,\"null\":null}]}").validate();
        } catch (JSONException e) {
            thrown = true;
        }
        assertFalse(thrown);
    }

    @Test
    public void validate_test_quotation() throws Throwable {
        boolean thrown = false;
        try {
            JSONValidator.from("{noQuotationMarksError}").validate();
        } catch (JSONException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void validate_test_colon() throws Throwable {
        boolean thrown = false;
        try {
            JSONValidator.from("{\"colonError\"}").validate();
        } catch (JSONException e) {
            thrown = true;
        }
        assertTrue(thrown);

    }

    @Test
    public void validate_test_bracket() throws Throwable {
        boolean thrown = false;
        try {
            JSONValidator.from("[1}").validate();
        } catch (JSONException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void validate_test_num1() throws Throwable {
        boolean thrown = false;
        try {
            JSONValidator.from("-a").validate();
        } catch (JSONException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void validate_test_num2() throws Throwable {
        boolean thrown = false;
        try {
            JSONValidator.from("1.a1").validate();
        } catch (JSONException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void validate_test_num3() throws Throwable {
        boolean thrown = false;
        try {
            JSONValidator.from("1.e1").validate();
        } catch (JSONException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void validate_test_num4() throws Throwable {
        boolean thrown = false;
        try {
            JSONValidator.from("+1").validate();
        } catch (JSONException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void validate_test_num5() throws Throwable {
        boolean thrown = false;
        try {
            JSONValidator.from("1ea").validate();
        } catch (JSONException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void validate_test_tfn() throws Throwable {
        boolean thrown = false;
        try {
            JSONValidator.from("trua").validate();
        } catch (JSONException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}