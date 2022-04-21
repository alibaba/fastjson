package com.alibaba.json.bvt.issue_4000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import org.junit.Test;
import junit.framework.TestCase;

public class Issue4069 extends TestCase {
    @Test
    public void test_for_issue4069_0() {
        String json_string = "[[{\"value\": \"aaa\",\"key\": \"input_aKLYFkHNhPk0\"},{\"value\": \"222\",\"key\": \"number_pPvGTENKofUM\"}]," +
                "[{\"value\": \"ffdf\",\"key\": \"input_aKLYFkHNhPk0\"},{\"value\": \"1212\",\"key\": \"number_pPvGTENKofUM\"}]]";
        assertTrue(JSON.isValidArray(json_string));
        assertEquals("Array", JSONValidator.from(json_string).getType().toString());

        assertTrue(JSON.isValidArray("[[]]"));
        assertEquals("Array", JSONValidator.from("[[]]").getType().toString());
    }

    @Test
    public void test_for_issue4069_1() {
        assertFalse(JSON.isValidArray("["));
        assertFalse(JSON.isValidArray("[[]"));
    }
}
