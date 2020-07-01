package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSONValidator;
import junit.framework.TestCase;

public class Issue3267 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONValidator v = JSONValidator.from("113{}[]");
        v.setSupportMultiValue(false);
        assertFalse(
                v.validate());

        assertEquals(JSONValidator.Type.Value, v.getType());
    }
}
