package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;


public class SmartMatchTest_snake extends TestCase {
    
    public void test_0() throws Exception {
        String text = "{\"person_id\":1001}";

        VO vo = JSON.parseObject(text, VO.class);
        Assert.assertEquals(1001, vo.personId);
    }

    public static class VO {
        public int personId;
    }
}
