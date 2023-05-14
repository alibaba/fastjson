package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

import java.math.BigDecimal;

public class JSONPath_min extends TestCase {
    public void test_max() throws Exception {
        Object root = JSON.parse("[1,3,9, 5, 2, 4]");
        assertEquals(1, JSONPath.eval(root, "$.min()"));
    }

    public void test_max_1() throws Exception {
        Object root = JSON.parse("[1,6,7L,3,8,9.1, 5, 2L, 4]");
        assertEquals(1, JSONPath.eval(root, "$.min()"));
    }

    public void test_max_2() throws Exception {
        Object root = JSON.parse("[1,6,7L,3,3.1D,8,9.1F, 5, 2L, 4]");
        assertEquals(1, JSONPath.eval(root, "$.min()"));
    }

    public void test_max_3() throws Exception {
        Object root = JSON.parse("[1,6,7L,3,3.1F,8,9.1F, 5, 2L, 4]");
        assertEquals(1, JSONPath.eval(root, "$.min()"));
    }

    public void test_max_4() throws Exception {
        Object root = JSON.parse("['1', '111', '2']");
        assertEquals("1", JSONPath.eval(root, "$.min()"));
    }
}
