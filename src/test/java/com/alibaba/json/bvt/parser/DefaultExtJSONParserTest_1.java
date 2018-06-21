package com.alibaba.json.bvt.parser;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.json.test.entity.TestEntity;

public class DefaultExtJSONParserTest_1 extends TestCase {

    public void test_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{\"f1\":true}");
        TestEntity entity = parser.parseObject(TestEntity.class);
        Assert.assertEquals(true, entity.isF1());
    }

    public void test_1() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{\"f2\":true}");
        TestEntity entity = parser.parseObject(TestEntity.class);
        Assert.assertEquals(Boolean.TRUE, entity.getF2());
    }

    public void f_test_2() throws Exception {
        TestEntity a = new TestEntity();
        a.setF1(true);
        a.setF2(Boolean.TRUE);
        a.setF3((byte) 123);
        a.setF4((byte) 123);
        a.setF5((short) 123);
        a.setF6((short) 123);
        a.setF7((int) 123);
        a.setF8((int) 123);
        a.setF9((long) 123);
        a.setF10((long) 123);
        a.setF11(new BigInteger("123"));
        a.setF12(new BigDecimal("123"));
        a.setF13("abc");
        a.setF14(null);
        a.setF15(12.34F);
        a.setF16(12.35F);
        a.setF17(12.345D);
        a.setF18(12.345D);

        String text = JSON.toJSONString(a);
        System.out.println(text);

        TestEntity b = new TestEntity();
        {
            DefaultJSONParser parser = new DefaultJSONParser(text);
            parser.parseObject(b);
        }

        Assert.assertEquals("f1", a.isF1(), b.isF1());
        Assert.assertEquals("f2", a.getF2(), b.getF2());
        Assert.assertEquals("f3", a.getF3(), b.getF3());
        Assert.assertEquals("f4", a.getF4(), b.getF4());
        Assert.assertEquals("f5", a.getF5(), b.getF5());
        Assert.assertEquals("f6", a.getF6(), b.getF6());
        Assert.assertEquals("f7", a.getF7(), b.getF7());
        Assert.assertEquals("f8", a.getF8(), b.getF8());
        Assert.assertEquals("f9", a.getF9(), b.getF9());
        Assert.assertEquals(a.getF10(), b.getF10());
        Assert.assertEquals(a.getF11(), b.getF11());
        Assert.assertEquals(a.getF12(), b.getF12());
        Assert.assertEquals(a.getF13(), b.getF13());
        Assert.assertEquals(a.getF14(), b.getF14());
        Assert.assertEquals(a.getF15(), b.getF15());
        Assert.assertEquals(a.getF16(), b.getF16());
        Assert.assertEquals(a.getF17(), b.getF17());
        Assert.assertEquals(a.getF18(), b.getF18());

    }
}
