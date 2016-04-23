package com.alibaba.json.bvt.parser.field;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class PublicFieldTest_short extends TestCase {

    public static class VO {

        public short id;
    }

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.id = 1234;
        
        String str = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(str, VO.class);
        
        Assert.assertEquals(vo1.id, vo.id);
    }
}
