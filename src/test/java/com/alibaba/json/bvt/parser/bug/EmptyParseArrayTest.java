package com.alibaba.json.bvt.parser.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class EmptyParseArrayTest extends TestCase {
    public void test_0() throws Exception {
        Assert.assertNull(JSON.parseArray("", VO.class));
    }
    
    public static class VO {
        
    }
}
