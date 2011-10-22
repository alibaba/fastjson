package com.alibaba.json.bvt.parser;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.json.test.entity.TestEntity;

public class DefaultExtJSONParserTest_1 extends TestCase {

    public void test_0() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("{\"f1\":true}");
        TestEntity entity = parser.parseObject(TestEntity.class);
        Assert.assertEquals(true, entity.isF1());
    }

    public void test_1() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("{\"f2\":true}");
        TestEntity entity = parser.parseObject(TestEntity.class);
        Assert.assertEquals(Boolean.TRUE, entity.getF2());
    }

    public void test_2() throws Exception {
        TestEntity e0 = new TestEntity();
        e0.setF1(true);
        e0.setF2(Boolean.TRUE);
        e0.setF3((byte) 123);
        e0.setF4((byte) 123);
        e0.setF5((short) 123);
        e0.setF6((short) 123);
        e0.setF7((int) 123);
        e0.setF8((int) 123);
        e0.setF9((long) 123);
        e0.setF10((long) 123);
        e0.setF11(new BigInteger("123"));
        e0.setF12(new BigDecimal("123"));
        e0.setF13("abc");
        e0.setF14(null);
        e0.setF15(12.34F);
        e0.setF16(12.34F);
        e0.setF17(12.345D);
        e0.setF18(12.345D);

        String text = JSON.toJSONString(e0);
        System.out.println(text);

        TestEntity e1 = new TestEntity();
        {
            DefaultExtJSONParser parser = new DefaultExtJSONParser(text);
            parser.parseObject(e1);
        }

        Assert.assertEquals(e0.isF1(), e1.isF1());
        Assert.assertEquals(e0.getF2(), e1.getF2());
        Assert.assertEquals(e0.getF3(), e1.getF3());
        Assert.assertEquals(e0.getF4(), e1.getF4());
        Assert.assertEquals(e0.getF5(), e1.getF5());
        Assert.assertEquals(e0.getF6(), e1.getF6());
        Assert.assertEquals(e0.getF7(), e1.getF7());
        Assert.assertEquals(e0.getF8(), e1.getF8());
        Assert.assertEquals(e0.getF9(), e1.getF9());
        Assert.assertEquals(e0.getF10(), e1.getF10());
        Assert.assertEquals(e0.getF11(), e1.getF11());
        Assert.assertEquals(e0.getF12(), e1.getF12());
        Assert.assertEquals(e0.getF13(), e1.getF13());
        Assert.assertEquals(e0.getF14(), e1.getF14());
        Assert.assertEquals(e0.getF15(), e1.getF15());
        Assert.assertEquals(e0.getF16(), e1.getF16());
        Assert.assertEquals(e0.getF17(), e1.getF17());
        Assert.assertEquals(e0.getF18(), e1.getF18());

    }
}
