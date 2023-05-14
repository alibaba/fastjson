package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_479 extends TestCase {

    public void test_for_issue_blankinput() throws Exception {
        VO vo = JSON.parseObject("", VO.class);
        Assert.assertNull(vo);
    }

    public void test_for_issue() throws Exception {
        VO vo = JSON.parseObject("{\"doubleParam\":\"\",\"floatParam\":\"\",\"intParam\":\"\",\"longParam\":\"\"}",
                                 VO.class);
        Assert.assertTrue(vo.doubleParam == 0);
        Assert.assertTrue(vo.floatParam == 0);
        Assert.assertTrue(vo.intParam == 0);
        Assert.assertTrue(vo.longParam == 0);
    }

    public void test_for_issue_private() throws Exception {
        V1 vo = JSON.parseObject("{\"doubleParam\":\"\",\"floatParam\":\"\",\"intParam\":\"\",\"longParam\":\"\"}",
                                 V1.class);
        Assert.assertTrue(vo.doubleParam == 0);
        Assert.assertTrue(vo.floatParam == 0);
        Assert.assertTrue(vo.intParam == 0);
        Assert.assertTrue(vo.longParam == 0);
    }

    public static class VO {

        public long  doubleParam;
        public float floatParam;
        public int   intParam;
        public long  longParam;
    }

    private static class V1 {

        public long  doubleParam;
        public float floatParam;
        public int   intParam;
        public long  longParam;
    }

    public static class V2 {

        private long  doubleParam;
        private float floatParam;
        private int   intParam;
        private long  longParam;

        public long getDoubleParam() {
            return doubleParam;
        }

        public void setDoubleParam(long doubleParam) {
            this.doubleParam = doubleParam;
        }

        public float getFloatParam() {
            return floatParam;
        }

        public void setFloatParam(float floatParam) {
            this.floatParam = floatParam;
        }

        public int getIntParam() {
            return intParam;
        }

        public void setIntParam(int intParam) {
            this.intParam = intParam;
        }

        public long getLongParam() {
            return longParam;
        }

        public void setLongParam(long longParam) {
            this.longParam = longParam;
        }

    }
}
