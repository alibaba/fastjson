package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.util.TypeUtils;


public class Issue141 extends TestCase {
    public void test_for_issue() throws Exception {
        Assert.assertFalse(TypeUtils.castToBoolean("0").booleanValue());
    }
}
