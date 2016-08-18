package com.alibaba.json.bvt.parser.deser.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestASM_object extends TestCase {

    public void test_asm() throws Exception {
        V0 v = new V0();
        String text = JSON.toJSONString(v);
        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v.getValue().getValue(), v1.getValue().getValue());
    }

    public static class V0 {

        private V1 value = new V1();

        public V1 getValue() {
            return value;
        }

        public void setValue(V1 value) {
            this.value = value;
        }

    }

    public static class V1 {

        private int value = 1234;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
