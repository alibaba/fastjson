package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Collections;

public class JSONPath_array_length extends TestCase {
    public void test_list_size() throws Exception {
        Assert.assertEquals(0, JSONPath.eval(new JSONArray(), "$.length"));
    }

    public void test_list_size1() throws Exception {
        Assert.assertEquals(0, JSONPath.eval(new Object[0], "$.length()"));
    }
}
