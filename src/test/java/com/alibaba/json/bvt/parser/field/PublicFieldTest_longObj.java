package com.alibaba.json.bvt.parser.field;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class PublicFieldTest_longObj extends TestCase {

    public static class VO {

        public Long id;
    }

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.id = 1234L;
        
        String str = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(str, VO.class);
        
        Assert.assertEquals(vo1.id, vo.id);
    }
}
