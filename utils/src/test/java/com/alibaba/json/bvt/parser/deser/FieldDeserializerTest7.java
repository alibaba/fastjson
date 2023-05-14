package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;


public class FieldDeserializerTest7 extends TestCase {
    public void test_0 () throws Exception {
        Assert.assertTrue(33F == JSON.parseObject("{\"id\":33\t}", VO.class).id);
        Assert.assertTrue(33F == JSON.parseObject("{\"id\":33\t}\n\t", VO.class).id);
        Assert.assertTrue(33F == JSON.parseObject("{\"id\":33 }", V1.class).id);
        Assert.assertTrue(33F == JSON.parseObject("{\"id\":33 }\n\t", V1.class).id);
        Assert.assertTrue(33F == JSON.parseObject("{\"id\":33L}", V1.class).id);
    }
    
    public static class VO {
        public double id;
    }
    
    private static class V1 {
        public double id;
    }
}
