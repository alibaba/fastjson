package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class FloatTest extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals("null", JSON.toJSONString(Float.NaN));
        Assert.assertEquals("null", JSON.toJSONString(Double.NaN));
        Assert.assertEquals("null", JSON.toJSONString(Float.POSITIVE_INFINITY));
        Assert.assertEquals("null", JSON.toJSONString(Float.NEGATIVE_INFINITY));
        Assert.assertEquals("null", JSON.toJSONString(Double.NaN));
        Assert.assertEquals("null", JSON.toJSONString(Double.POSITIVE_INFINITY));
        Assert.assertEquals("null", JSON.toJSONString(Double.NEGATIVE_INFINITY));
        Assert.assertEquals("null", JSON.toJSONString(new Float(Float.NaN)));
        Assert.assertEquals("null", JSON.toJSONString(new Double(Double.NaN)));
        
        //Assert.assertEquals("{\"f1\":null,\"f2\":null}", JSON.toJSONString(new Bean()));
        //Assert.assertEquals("{\"f1\":null,\"f2\":null}", JSON.toJSONString(new Bean(Float.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)));
        //Assert.assertEquals("{\"f1\":null,\"f2\":null}", JSON.toJSONString(new Bean(Float.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)));
        Assert.assertEquals(null, JSON.parseObject(JSON.toJSONString(new Bean())).get("f1"));
        Assert.assertEquals(null, JSON.parseObject(JSON.toJSONString(new Bean())).get("f2"));
        
        Assert.assertEquals(null, JSON.parseObject(JSON.toJSONString(new Bean(Float.POSITIVE_INFINITY, Double.POSITIVE_INFINITY))).get("f1"));
        Assert.assertEquals(null, JSON.parseObject(JSON.toJSONString(new Bean(Float.POSITIVE_INFINITY, Double.POSITIVE_INFINITY))).get("f2"));
        
        Assert.assertEquals(null, JSON.parseObject(JSON.toJSONString(new Bean(Float.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY))).get("f1"));
        Assert.assertEquals(null, JSON.parseObject(JSON.toJSONString(new Bean(Float.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY))).get("f2"));
    }

    public static class Bean {

        private float  f1 = Float.NaN;
        private double f2 = Double.NaN;
        
        public Bean() {
            
        }
        
        public Bean(float f1, double f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        public float getF1() {
            return f1;
        }

        public void setF1(float f1) {
            this.f1 = f1;
        }

        public double getF2() {
            return f2;
        }

        public void setF2(double f2) {
            this.f2 = f2;
        }

    }
}
