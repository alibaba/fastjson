package com.alibaba.json.bvt.parser.deser.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestASM_boolean extends TestCase {

    public void test_asm() throws Exception {
        V0 v = new V0();
        String text = JSON.toJSONString(v);
        V0 v1 = JSON.parseObject(text, V0.class);
        
        Assert.assertEquals(v.isValue(), v1.isValue());
    }

    public static class V0 {

        private boolean value = true;

        public boolean isValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

    }
}
