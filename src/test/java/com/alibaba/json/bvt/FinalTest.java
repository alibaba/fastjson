package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class FinalTest extends TestCase {
    public void test_final() throws Exception {
        VO vo = new VO();
        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"value\":1001}", text);
        JSON.parseObject(text, VO.class);
        JSON.parseObject("{\"id\":1001,\"value\":1001}", VO.class);
    }
    
    
    public static class VO {
        public final static int id = 1001;
        public final int value = 1001;
    }
}
