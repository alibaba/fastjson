package com.alibaba.json.test.bvt.serializer;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestInnerClass1 extends TestCase {

    public void test_inner() throws Exception {
        VO vo = new VO();
        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"value\":234}", text);
    }

    private class VO {

        private int value = 234;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
