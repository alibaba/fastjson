package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestPivateStaticClass extends TestCase {

    public void test_inner() throws Exception {
        VO vo = new VO();
        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"value\":234}", text);
        
        VO v1 = JSON.parseObject(text, VO.class);
    }

    private static class VO {

        private int value = 234;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
