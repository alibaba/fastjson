package com.alibaba.json.bvt.parser.taobao;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class IntAsStringTest extends TestCase {
    public void test_0 () throws Exception {
        VO vo = JSON.parseObject("{\"value\":\"1001\"}", VO.class);
        Assert.assertEquals(1001, vo.value);
    }
    
    public static class VO {
        public int value;
    }
}
