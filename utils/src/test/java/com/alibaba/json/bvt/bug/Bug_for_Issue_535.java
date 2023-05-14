package com.alibaba.json.bvt.bug;

import java.math.BigDecimal;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class Bug_for_Issue_535 extends TestCase {
    public void test_for_issue() throws Exception {
        TestPOJO testPOJO = new TestPOJO();
        testPOJO.setA("a");
        testPOJO.setB(new BigDecimal("1234512312312312312312"));
        String s = JSON.toJSONString(testPOJO);
        System.out.println(s);
        
        TestPOJO vo2 = JSON.parseObject(s, TestPOJO.class, Feature.UseBigDecimal);
        Assert.assertEquals(testPOJO.getB(), vo2.getB());
    }

    public static class TestPOJO {

        private String     a;
        private BigDecimal b;
        // getter and setter

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public BigDecimal getB() {
            return b;
        }

        public void setB(BigDecimal b) {
            this.b = b;
        }

    }

}
