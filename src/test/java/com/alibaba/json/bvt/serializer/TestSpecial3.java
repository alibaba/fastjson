package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestSpecial3 extends TestCase {

    public void test_0() throws Exception {
        StringBuilder buf = new StringBuilder();
        buf.append('\r');
        buf.append('\r');
        for (int i = 0; i < 1000; ++i) {
            buf.append('\u000B');
        }

        VO vo = new VO();
        vo.setValue(buf.toString());
        
        System.out.println(JSON.toJSONString(vo));
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
