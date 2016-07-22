package com.alibaba.json.bvt.path;

import java.util.Collections;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_map_size extends TestCase {
    public void test_list_size() throws Exception {
        Assert.assertEquals(0, JSONPath.eval(Collections.emptyMap(), "$.size"));
    }

    public void test_list_size1() throws Exception {
        Assert.assertEquals(0, JSONPath.eval(Collections.emptyMap(), "$.size()"));
    }
}
