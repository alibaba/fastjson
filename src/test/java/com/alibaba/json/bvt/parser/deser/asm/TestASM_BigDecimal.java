package com.alibaba.json.bvt.parser.deser.asm;

import java.math.BigDecimal;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestASM_BigDecimal extends TestCase {

    public void test_decimal() throws Exception {
        V0 v = new V0();
        String text = JSON.toJSONString(v, SerializerFeature.UseSingleQuotes);
        Assert.assertEquals("{}", text);
    }

    public void test_decimal_1() throws Exception {
        V0 v = new V0();
        v.setDecimal(new BigDecimal("123"));
        String text = JSON.toJSONString(v, SerializerFeature.UseSingleQuotes);
        Assert.assertEquals("{'decimal':123}", text);
    }
    

    public void test_decimal_2() throws Exception {
        V1 v = new V1();
        v.setId(123);
        String text = JSON.toJSONString(v, SerializerFeature.UseSingleQuotes);

        Assert.assertEquals("{'id':123}", text);
    }
    
    public void test_decimal_3() throws Exception {
        V1 v = new V1();
        v.setId(123);
        String text = JSON.toJSONString(v, SerializerFeature.UseSingleQuotes, SerializerFeature.WriteMapNullValue);
        System.out.println(text);
        
        Assert.assertEquals("{'decimal':null,'id':123}", text);
    }

    public static class V0 {

        private BigDecimal decimal;

        public BigDecimal getDecimal() {
            return decimal;
        }

        public void setDecimal(BigDecimal decimal) {
            this.decimal = decimal;
        }

    }

    public static class V1 {

        private int        id;
        private BigDecimal decimal;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public BigDecimal getDecimal() {
            return decimal;
        }

        public void setDecimal(BigDecimal decimal) {
            this.decimal = decimal;
        }

    }
}
