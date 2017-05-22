package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_291 extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_issue_291.");
    }

    public void test_for_issue() throws Exception {
        String text = "{\"id\":123,\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_issue_291$VO\",\"value\":54321}";
        
        Object o = JSON.parse(text);
        Assert.assertEquals(VO.class, o.getClass());
    }
    
    public void test_for_issue_private() throws Exception {
        String text = "{\"id\":123,\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_issue_291$VO\",\"value\":54321}";
        
        Object o = JSON.parse(text);
        Assert.assertEquals(VO.class, o.getClass());
    }
    
    public static class VO {
        public int id;
        public int value;
    }
    
    public static class V1 {
        public int id;
        public int value;
    }
}
