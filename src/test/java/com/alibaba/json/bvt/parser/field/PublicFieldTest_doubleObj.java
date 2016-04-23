package com.alibaba.json.bvt.parser.field;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class PublicFieldTest_doubleObj extends TestCase {

    public static class VO {

        public Double id;
    }

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.id = 123.45D;
        
        String str = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(str, VO.class);
        
        Assert.assertTrue(vo1.id.doubleValue() == vo.id.doubleValue());
    }
}
