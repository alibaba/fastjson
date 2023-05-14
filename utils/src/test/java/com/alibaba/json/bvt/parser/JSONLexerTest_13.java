package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class JSONLexerTest_13 extends TestCase {

    public void test_e() throws Exception {
        Assert.assertTrue(123e3D == JSON.parseObject("{\"vo\":{\"type\":123e3}}", A.class).getVo().getType());
    }

    public void test_E() throws Exception {
        Assert.assertTrue(123e3D == JSON.parseObject("{\"vo\":{\"type\":123E3}}", A.class).getVo().getType());
    }
    
    public void test_e_plus() throws Exception {
        Assert.assertTrue(123e3D == JSON.parseObject("{\"vo\":{\"type\":123e+3}}", A.class).getVo().getType());
    }
    
    public void test_E_plus() throws Exception {
        Assert.assertTrue(123e3D == JSON.parseObject("{\"vo\":{\"type\":123E+3}}", A.class).getVo().getType());
    }
    
    public void test_e_minus() throws Exception {
        Assert.assertTrue(123e-3D == JSON.parseObject("{\"vo\":{\"type\":123e-3}}", A.class).getVo().getType());
    }
    
    public void test_E_minus() throws Exception {
        Assert.assertTrue(123e-3D == JSON.parseObject("{\"vo\":{\"type\":123E-3}}", A.class).getVo().getType());
    }

    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"vo\":{\"type\":123]", A.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class A {

        private VO vo;

        public VO getVo() {
            return vo;
        }

        public void setVo(VO vo) {
            this.vo = vo;
        }

    }

    public static class VO {

        public VO(){

        }

        private double type;

        public double getType() {
            return type;
        }

        public void setType(double type) {
            this.type = type;
        }

    }
}
