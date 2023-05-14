package com.alibaba.json.bvt.serializer;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class TestSpecial5 extends TestCase {

    public void test_1() throws Exception {
        StringBuilder buf = new StringBuilder();
        buf.append(' ');
        for (int i = 0; i < 1000; ++i) {
            buf.append((char) 160);
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
