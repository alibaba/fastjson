package com.alibaba.json.bvt.parser.field;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class PublicFieldTest_Integer_null extends TestCase {

    public static class VO {

        public Integer id;
    }

    public void test_parse() throws Exception {
        VO vo = JSON.parseObject("{\"id\":null}", VO.class);
        
        Assert.assertNull(vo.id);
    }
}
