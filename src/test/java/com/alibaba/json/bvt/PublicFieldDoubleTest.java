package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class PublicFieldDoubleTest extends TestCase {

    public static class VO {

        public double id;
    }

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.id = 12.34;
        
        String str = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(str, VO.class);
        
        Assert.assertTrue(vo1.id == vo.id);
    }

    public void test_nan() throws Exception {
        JSON.parseObject("{\"id\":NaN}", VO.class);
    }
}
