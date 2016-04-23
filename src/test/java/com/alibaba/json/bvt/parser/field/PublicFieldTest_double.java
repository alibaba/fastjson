package com.alibaba.json.bvt.parser.field;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class PublicFieldTest_double extends TestCase {

    public static class VO {

        public double id;
    }

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.id = 123.45;
        
        String str = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(str, VO.class);
        
        Assert.assertTrue(vo1.id == vo.id);
    }
}
