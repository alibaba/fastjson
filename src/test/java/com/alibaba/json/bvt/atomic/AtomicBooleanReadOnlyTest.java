package com.alibaba.json.bvt.atomic;

import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class AtomicBooleanReadOnlyTest extends TestCase {

    public void test_codec_null() throws Exception {
        V0 v = new V0(true);

        String text = JSON.toJSONString(v);
        Assert.assertEquals("{\"value\":true}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue().get(), v.getValue().get());
    }

    public static class V0 {

        private final AtomicBoolean value;

        public V0(){
            this(false);
        }

        public V0(boolean value){
            this.value = new AtomicBoolean(value);
        }

        public AtomicBoolean getValue() {
            return value;
        }

    }
}
