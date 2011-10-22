package com.alibaba.json.bvt.parser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class NullCheckTest extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals(null, JSON.parse(null));
        Assert.assertEquals(null, JSON.parse(""));
        Assert.assertEquals(null, JSON.parse(" "));
    }
}
