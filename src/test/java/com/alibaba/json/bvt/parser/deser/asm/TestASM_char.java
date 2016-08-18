package com.alibaba.json.bvt.parser.deser.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestASM_char extends TestCase {

    public void test_asm() throws Exception {
        V0 v = new V0();
        String text = JSON.toJSONString(v);
        V0 v1 = JSON.parseObject(text, V0.class);
        
        Assert.assertEquals(v.getValue(), v1.getValue());
    }

    public static class V0 {

        private char value = '中';

        public char getValue() {
            return value;
        }

        public void setValue(char i) {
            this.value = i;
        }

    }
}
