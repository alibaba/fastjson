package com.alibaba.json.bvt.issue_3500;

import com.alibaba.fastjson.JSONValidator;
import junit.framework.TestCase;

public class Issue3516 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONValidator validator = JSONValidator.from("{}");
        assertEquals(JSONValidator.Type.Object, validator.getType());
        assertTrue(validator.validate());
    }
}
