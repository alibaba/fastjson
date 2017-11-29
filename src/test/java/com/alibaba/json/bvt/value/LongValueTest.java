package com.alibaba.json.bvt.value;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class LongValueTest extends TestCase {

    public void test_value() {
        long step = ((long) Integer.MAX_VALUE) * 1000 * 1000;
        for (long i = Long.MIN_VALUE; i <= 0; i += step) {
            VO vo = new VO();
            vo.value = i;
            String text = JSON.toJSONString(vo);
            VO vo2 = JSON.parseObject(text, VO.class);
            Assert.assertEquals(vo.value, vo2.value);
        }
        for (long i = Long.MAX_VALUE; i >= 0; i -= step) {
            VO vo = new VO();
            vo.value = i;
            String text = JSON.toJSONString(vo);
            VO vo2 = JSON.parseObject(text, VO.class);
            Assert.assertEquals(vo.value, vo2.value);
        }
    }

    public void test_value_1() {
        long step = ((long) Integer.MAX_VALUE) * 1000 * 1000;
        for (long i = Long.MIN_VALUE; i <= 0; i += step) {
            V1 vo = new V1();
            vo.value = i;
            String text = JSON.toJSONString(vo);
            V1 vo2 = JSON.parseObject(text, V1.class);
            Assert.assertEquals(vo.value, vo2.value);
        }
        for (long i = Long.MAX_VALUE; i >= 0; i -= step) {
            V1 vo = new V1();
            vo.value = i;
            String text = JSON.toJSONString(vo);
            V1 vo2 = JSON.parseObject(text, V1.class);
            Assert.assertEquals(vo.value, vo2.value);
        }
    }
    
    public void test_value_2() {
        long step = ((long) Integer.MAX_VALUE) * 1000 * 1000;
        for (long i = Long.MIN_VALUE; i <= 0; i += step) {
            V2 vo = new V2();
            vo.value = i;
            String text = JSON.toJSONString(vo);
            V2 vo2 = JSON.parseObject(text, V2.class);
            Assert.assertEquals(vo.value, vo2.value);
        }
        for (long i = Long.MAX_VALUE; i >= 0; i -= step) {
            V2 vo = new V2();
            vo.value = i;
            String text = JSON.toJSONString(vo);
            V2 vo2 = JSON.parseObject(text, V2.class);
            Assert.assertEquals(vo.value, vo2.value);
        }
    }

    public static class VO {

        public long value;
    }

    static class V1 {

        public long value;
    }

    public static class V2 {

        private long value;

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

    }
}
