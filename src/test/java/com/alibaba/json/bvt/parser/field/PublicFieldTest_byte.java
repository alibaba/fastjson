package com.alibaba.json.bvt.parser.field;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class PublicFieldTest_byte extends TestCase {

    public static class VO {

        public byte id;
    }

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.id = 12;
        
        String str = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(str, VO.class);
        
        Assert.assertEquals(vo1.id, vo.id);
    }
}
