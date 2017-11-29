package com.alibaba.json.bvt.atomic;

import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class AtomicIntegerReadOnlyTest extends TestCase {

    public void test_codec_null() throws Exception {
        V0 v = new V0(123);

        String text = JSON.toJSONString(v);
        Assert.assertEquals("{\"value\":123}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue().intValue(), v.getValue().intValue());
    }

    public static class V0 {

        private final AtomicInteger value;

        public V0(){
            this(0);
        }

        public V0(int value){
            this.value = new AtomicInteger(value);
        }

        public AtomicInteger getValue() {
            return value;
        }

    }
}
