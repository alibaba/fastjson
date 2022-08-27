package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class TestSpecial4 extends TestCase {

    public void test_0() throws Exception {
        StringBuilder buf = new StringBuilder();
        buf.append('\r');
        buf.append('\r');
        for (int i = 0; i < 1000; ++i) {
            buf.append('\u2028');
        }

        VO vo = new VO();
        vo.setValue(buf.toString());
        
        String text = JSON.toJSONString(vo);
        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.value, vo2.value);
    }

    public static class VO {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
