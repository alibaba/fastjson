package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class Issue64 extends TestCase {
    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.foo = "xxxxxx";
        
        String text = JSON.toJSONString(vo, SerializerFeature.BeanToArray);
        
        Assert.assertEquals("[\"xxxxxx\"]", text);
        
        VO vo2 = JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        Assert.assertEquals(vo2.foo, vo.foo);
    }
    
    public static class VO {
        public String foo = "bar";
    }
}
