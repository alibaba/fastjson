package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.json.bvt.parser.deser.FactoryTest.V1;

import junit.framework.TestCase;

public class FactoryTest_error extends TestCase {

    public void test_factory1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"b\":true,\"i\":33,\"l\":34,\"f\":45.}").toJavaObject(V1.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class V1 {

        private boolean b;
        private int     i;
        private long    l;
        private float   f;

        private V1(boolean b){
            this.b = b;
        }

        @JSONCreator
        public static V1 create(@JSONField(name = "b") boolean b, @JSONField(name = "i") int i,
                                @JSONField(name = "l") long l, @JSONField(name = "f") float f) {
            throw new IllegalStateException();
        }

        public float getF() {
            return f;
        }

        public boolean isB() {
            return b;
        }

        public int getI() {
            return i;
        }

        public long getL() {
            return l;
        }

    }
}
