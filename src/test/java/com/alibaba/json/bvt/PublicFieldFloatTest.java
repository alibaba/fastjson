package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class PublicFieldFloatTest extends TestCase {

    public static class VO {

        public float id;
    }

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.id = 123.4F;
        
        String str = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(str, VO.class);
        
        Assert.assertTrue(vo1.id == vo.id);
    }
}
