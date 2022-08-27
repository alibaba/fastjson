package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSONValidator;
import junit.framework.TestCase;

public class Issue3343 extends TestCase {
    public void test_for_issue() throws Exception {
        assertFalse(
                JSONValidator.from("{\"name\":\"999}")
                        .validate());

        assertTrue(
                JSONValidator.from("false")
                        .validate());
        assertEquals(JSONValidator.Type.Value,
                JSONValidator.from("false")
                        .getType());

        assertTrue(
                JSONValidator.from("999").validate());
        assertEquals(JSONValidator.Type.Value,
                JSONValidator.from("999")
                        .getType());
    }
}
