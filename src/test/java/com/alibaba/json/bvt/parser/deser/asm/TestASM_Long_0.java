package com.alibaba.json.bvt.parser.deser.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestASM_Long_0 extends TestCase {

    public void test_asm() throws Exception {
        V0 v = new V0();
        String text = JSON.toJSONString(v);
        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v.getI(), v1.getI());
    }

    public static class V0 {

        private Long i = 12L;

        public Long getI() {
            return i;
        }

        public void setI(Long i) {
            this.i = i;
        }

    }
}
