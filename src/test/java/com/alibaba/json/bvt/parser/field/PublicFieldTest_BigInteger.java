package com.alibaba.json.bvt.parser.field;

import java.math.BigInteger;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class PublicFieldTest_BigInteger extends TestCase {

    public static class VO {

        public BigInteger id;
    }

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.id = new BigInteger("1234567890123456789012345678901234567890");
        
        String str = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(str, VO.class);
        
        Assert.assertEquals(vo1.id, vo.id);
    }
}
