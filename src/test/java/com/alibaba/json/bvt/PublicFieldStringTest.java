package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class PublicFieldStringTest extends TestCase {

    public static class VO {

        public String id;
    }

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.id = "x12345";
        
        String str = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(str, VO.class);
        
        Assert.assertEquals(vo1.id, vo.id);
    }
}
