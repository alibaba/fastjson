package com.alibaba.json.bvt.parser.deser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class IntegerParseTest extends TestCase {
    public void test_l () throws Exception {
        Assert.assertEquals(Long.valueOf(12), JSON.parseObject("12L", long.class));
        Assert.assertEquals(Integer.valueOf(12), JSON.parseObject("12L", int.class));
        Assert.assertEquals(new Short((short) 12), JSON.parseObject("12L", short.class));
        Assert.assertEquals(new Byte((byte) 12), JSON.parseObject("12L", byte.class));
        Assert.assertEquals(new Float(12), JSON.parseObject("12L", float.class));
        Assert.assertEquals(new Double(12), JSON.parseObject("12L", double.class));
    }
    
    public void test_s () throws Exception {
        Assert.assertEquals(Long.valueOf(12), JSON.parseObject("12S", long.class));
        Assert.assertEquals(Integer.valueOf(12), JSON.parseObject("12S", int.class));
        Assert.assertEquals(new Short((short) 12), JSON.parseObject("12S", short.class));
        Assert.assertEquals(new Byte((byte) 12), JSON.parseObject("12S", byte.class));
        Assert.assertEquals(new Float(12), JSON.parseObject("12S", float.class));
        Assert.assertEquals(new Double(12), JSON.parseObject("12S", double.class));
    }
    
    public void test_b () throws Exception {
        Assert.assertEquals(Long.valueOf(12), JSON.parseObject("12B", long.class));
        Assert.assertEquals(Integer.valueOf(12), JSON.parseObject("12B", int.class));
        Assert.assertEquals(new Short((short) 12), JSON.parseObject("12B", short.class));
        Assert.assertEquals(new Byte((byte) 12), JSON.parseObject("12B", byte.class));
        Assert.assertEquals(new Float(12), JSON.parseObject("12B", float.class));
        Assert.assertEquals(new Double(12), JSON.parseObject("12B", double.class));
    }
    
    public void test_f () throws Exception {
        Assert.assertEquals(Long.valueOf(12), JSON.parseObject("12F", long.class));
        Assert.assertEquals(Integer.valueOf(12), JSON.parseObject("12F", int.class));
        Assert.assertEquals(new Short((short) 12), JSON.parseObject("12F", short.class));
        Assert.assertEquals(new Byte((byte) 12), JSON.parseObject("12F", byte.class));
        Assert.assertEquals(new Float(12), JSON.parseObject("12F", float.class));
        Assert.assertEquals(new Double(12), JSON.parseObject("12F", double.class));
    }
    
    public void test_d () throws Exception {
        Assert.assertEquals(Long.valueOf(12), JSON.parseObject("12D", long.class));
        Assert.assertEquals(Integer.valueOf(12), JSON.parseObject("12D", int.class));
        Assert.assertEquals(new Short((short) 12), JSON.parseObject("12D", short.class));
        Assert.assertEquals(new Byte((byte) 12), JSON.parseObject("12D", byte.class));
        Assert.assertEquals(new Float(12), JSON.parseObject("12D", float.class));
        Assert.assertEquals(new Double(12), JSON.parseObject("12D", double.class));
    }
}
