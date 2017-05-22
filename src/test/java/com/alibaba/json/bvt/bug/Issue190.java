package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class Issue190 extends TestCase {

    public void test_for_issue() throws Exception {
        Assert.assertEquals(WebSoscketCommand.A, JSON.parseObject("\"A\"", WebSoscketCommand.class));
    }
    
    public static enum WebSoscketCommand {
        A, B, C
    }
}
