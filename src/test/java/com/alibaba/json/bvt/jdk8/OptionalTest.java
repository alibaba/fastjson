package com.alibaba.json.bvt.jdk8;

import java.util.Optional;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class OptionalTest extends TestCase {
    public void test_optional() throws Exception {
        Optional<Integer> val = Optional.of(3);
        
        String text = JSON.toJSONString(val);
        
        Assert.assertEquals("3", text);
    }
}
