package com.alibaba.json.bvt.parser.taobao;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class BooleanObjectFieldTest extends TestCase {
    public void test_0 () throws Exception {
        VO vo = JSON.parseObject("{\"value\":true}", VO.class);
        Assert.assertTrue(vo.value);
    }
    
    public static class VO {
        public Boolean value;
    }
}
