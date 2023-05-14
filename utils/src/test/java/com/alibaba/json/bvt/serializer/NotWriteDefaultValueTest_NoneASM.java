package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class NotWriteDefaultValueTest_NoneASM extends TestCase {

    public void test_for_byte() throws Exception {
        VO_Byte vo = new VO_Byte();
        String text = JSON.toJSONString(vo, SerializerFeature.NotWriteDefaultValue);
        Assert.assertEquals("{}", text);
    }

    public void test_for_short() throws Exception {
        VO_Short vo = new VO_Short();
        String text = JSON.toJSONString(vo, SerializerFeature.NotWriteDefaultValue);
        Assert.assertEquals("{}", text);
    }

    public void test_for_int() throws Exception {
        VO_Int vo = new VO_Int();
        String text = JSON.toJSONString(vo, SerializerFeature.NotWriteDefaultValue);
        Assert.assertEquals("{}", text);
    }

    public void test_for_long() throws Exception {
        VO_Long vo = new VO_Long();
        String text = JSON.toJSONString(vo, SerializerFeature.NotWriteDefaultValue);
        Assert.assertEquals("{}", text);
    }

    public void test_for_float() throws Exception {
        VO_Float vo = new VO_Float();
        String text = JSON.toJSONString(vo, SerializerFeature.NotWriteDefaultValue);
        Assert.assertEquals("{}", text);
    }

    public void test_for_double() throws Exception {
        VO_Double vo = new VO_Double();
        String text = JSON.toJSONString(vo, SerializerFeature.NotWriteDefaultValue);
        Assert.assertEquals("{}", text);
    }
    
    public void test_for_boolean() throws Exception {
        VO_Boolean vo = new VO_Boolean();
        vo.f1 = true;
        String text = JSON.toJSONString(vo, SerializerFeature.NotWriteDefaultValue);
        Assert.assertEquals("{\"f1\":true}", text);
    }

    private static class VO_Byte {

        private byte f0;
        private byte f1;

        public byte getF0() {
            return f0;
        }

        public void setF0(byte f0) {
            this.f0 = f0;
        }

        public byte getF1() {
            return f1;
        }

        public void setF1(byte f1) {
            this.f1 = f1;
        }

    }

    private static class VO_Short {

        private short f0;
        private short f1;

        public short getF0() {
            return f0;
        }

        public void setF0(short f0) {
            this.f0 = f0;
        }

        public short getF1() {
            return f1;
        }

        public void setF1(short f1) {
            this.f1 = f1;
        }

    }

    private static class VO_Int {

        private int f0;
        private int f1;

        public int getF0() {
            return f0;
        }

        public void setF0(int f0) {
            this.f0 = f0;
        }

        public int getF1() {
            return f1;
        }

        public void setF1(int f1) {
            this.f1 = f1;
        }
    }

    private static class VO_Long {

        private long f0;
        private long f1;

        public long getF0() {
            return f0;
        }

        public void setF0(long f0) {
            this.f0 = f0;
        }

        public long getF1() {
            return f1;
        }

        public void setF1(long f1) {
            this.f1 = f1;
        }

    }

    private static class VO_Float {

        private float f0;
        private float f1;

        public float getF0() {
            return f0;
        }

        public void setF0(float f0) {
            this.f0 = f0;
        }

        public float getF1() {
            return f1;
        }

        public void setF1(float f1) {
            this.f1 = f1;
        }

    }

    private static class VO_Double {

        private double f0;
        private double f1;

        public double getF0() {
            return f0;
        }

        public void setF0(double f0) {
            this.f0 = f0;
        }

        public double getF1() {
            return f1;
        }

        public void setF1(double f1) {
            this.f1 = f1;
        }

    }

    private static class VO_Boolean {

        private boolean f0;
        private boolean f1;

        public boolean isF0() {
            return f0;
        }

        public void setF0(boolean f0) {
            this.f0 = f0;
        }

        public boolean isF1() {
            return f1;
        }

        public void setF1(boolean f1) {
            this.f1 = f1;
        }

    }
}
