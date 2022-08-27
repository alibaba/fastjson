package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSONValidator;
import junit.framework.TestCase;

public class Issue3245 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONValidator v = JSONValidator.from("[]");
        v.validate();
        assertEquals(JSONValidator.Type.Array, v.getType());
    }

    public void test_for_issue_1() throws Exception {
        assertEquals(JSONValidator.Type.Array, JSONValidator.from("[]").getType());
    }
}
