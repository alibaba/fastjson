package com.alibaba.json.bvt.parser.deser;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;
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
        Assert.assertEquals(new BigDecimal(12), JSON.parseObject("12L", BigDecimal.class));
        Assert.assertEquals(new BigInteger("12"), JSON.parseObject("12L", BigInteger.class));
    }
    
    public void test_s () throws Exception {
        Assert.assertEquals(Long.valueOf(12), JSON.parseObject("12S", long.class));
        Assert.assertEquals(Integer.valueOf(12), JSON.parseObject("12S", int.class));
        Assert.assertEquals(new Short((short) 12), JSON.parseObject("12S", short.class));
        Assert.assertEquals(new Byte((byte) 12), JSON.parseObject("12S", byte.class));
        Assert.assertEquals(new Float(12), JSON.parseObject("12S", float.class));
        Assert.assertEquals(new Double(12), JSON.parseObject("12S", double.class));
        Assert.assertEquals(new BigDecimal(12), JSON.parseObject("12S", BigDecimal.class));
        Assert.assertEquals(new BigInteger("12"), JSON.parseObject("12S", BigInteger.class));
    }
    
    public void test_b () throws Exception {
        Assert.assertEquals(Long.valueOf(12), JSON.parseObject("12B", long.class));
        Assert.assertEquals(Integer.valueOf(12), JSON.parseObject("12B", int.class));
        Assert.assertEquals(new Short((short) 12), JSON.parseObject("12B", short.class));
        Assert.assertEquals(new Byte((byte) 12), JSON.parseObject("12B", byte.class));
        Assert.assertEquals(new Float(12), JSON.parseObject("12B", float.class));
        Assert.assertEquals(new Double(12), JSON.parseObject("12B", double.class));
        Assert.assertEquals(new BigDecimal(12), JSON.parseObject("12B", BigDecimal.class));
        Assert.assertEquals(new BigInteger("12"), JSON.parseObject("12B", BigInteger.class));
    }
    
    public void test_f () throws Exception {
        Assert.assertEquals(Long.valueOf(12), JSON.parseObject("12F", long.class));
        Assert.assertEquals(Integer.valueOf(12), JSON.parseObject("12F", int.class));
        Assert.assertEquals(new Short((short) 12), JSON.parseObject("12F", short.class));
        Assert.assertEquals(new Byte((byte) 12), JSON.parseObject("12F", byte.class));
        Assert.assertEquals(new Float(12), JSON.parseObject("12F", float.class));
        Assert.assertEquals(new Double(12), JSON.parseObject("12F", double.class));
        Assert.assertEquals(new BigDecimal(12), JSON.parseObject("12F", BigDecimal.class));
        Assert.assertEquals(new BigInteger("12"), JSON.parseObject("12F", BigInteger.class));
    }
    
    public void test_d () throws Exception {
        Assert.assertEquals(Long.valueOf(12), JSON.parseObject("12D", long.class));
        Assert.assertEquals(Integer.valueOf(12), JSON.parseObject("12D", int.class));
        Assert.assertEquals(new Short((short) 12), JSON.parseObject("12D", short.class));
        Assert.assertEquals(new Byte((byte) 12), JSON.parseObject("12D", byte.class));
        Assert.assertEquals(new Float(12), JSON.parseObject("12D", float.class));
        Assert.assertEquals(new Double(12), JSON.parseObject("12D", double.class));
        Assert.assertEquals(new BigDecimal(12), JSON.parseObject("12D", BigDecimal.class));
        Assert.assertEquals(new BigInteger("12"), JSON.parseObject("12D", BigInteger.class));
    }
    
    public void test_m () throws Exception {
        Assert.assertEquals(Long.valueOf(12), JSON.parseObject("12.", long.class));
        Assert.assertEquals(Integer.valueOf(12), JSON.parseObject("12.", int.class));
        Assert.assertEquals(new Short((short) 12), JSON.parseObject("12.", short.class));
        Assert.assertEquals(new Byte((byte) 12), JSON.parseObject("12.", byte.class));
        Assert.assertEquals(new Float(12), JSON.parseObject("12.", float.class));
        Assert.assertEquals(new Double(12), JSON.parseObject("12.", double.class));
        Assert.assertEquals(new BigDecimal(12), JSON.parseObject("12.", BigDecimal.class));
        Assert.assertEquals(new BigInteger("12"), JSON.parseObject("12.", BigInteger.class));
    }
}
