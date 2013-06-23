package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class PublicFieldLongTest extends TestCase {

    public static class VO {

        public long id;
    }

    public void test_codec() throws Exception {
        VO vo = new VO();
        vo.id = 1234;
        
        String str = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(str, VO.class);
        
        Assert.assertEquals(vo1.id, vo.id);
    }
}
