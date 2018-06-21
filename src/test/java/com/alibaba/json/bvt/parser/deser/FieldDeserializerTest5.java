package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;


public class FieldDeserializerTest5 extends TestCase {
    public void test_0 () throws Exception {
        Assert.assertEquals(33, JSON.parseObject("{\"id\":33\t}", VO.class).id);
        Assert.assertEquals(33, JSON.parseObject("{\"id\":33\t}\n\t", VO.class).id);
        Assert.assertEquals(33, JSON.parseObject("{\"id\":33 }", V1.class).id);
        Assert.assertEquals(33, JSON.parseObject("{\"id\":33 } ", V1.class).id);
        Assert.assertEquals(33, JSON.parseObject("{\"id\":33 }\n", V1.class).id);
        Assert.assertEquals(33, JSON.parseObject("{\"id\":33 }\t\n", V1.class).id);
        Assert.assertEquals(33, JSON.parseObject("{\"id\":33L}", V1.class).id);
    }
    
    public static class VO {
        public int id;
    }
    
    private static class V1 {
        public int id;
    }
}
