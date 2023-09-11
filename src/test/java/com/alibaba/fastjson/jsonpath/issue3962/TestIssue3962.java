package com.alibaba.fastjson.jsonpath.issue3962;

import com.alibaba.fastjson.JSONPath;
import org.junit.Assert;
import org.junit.Test;

public class TestIssue3962 {

    @Test
    public void testIssue3962() {
        Object val = JSONPath.eval("{\"a\": {\"b\": \"\"}}", "$.a.b.c");
        Assert.assertNull(val);
    }

}
