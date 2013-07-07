package com.alibaba.json.bvt;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class ArrayListFieldTest_1 extends TestCase {

    public void test_var() throws Exception {
        JSON.parseObject("{\"value\":[{}]}", V0.class);
        V0<A> v1 = JSON.parseObject("{\"value\":[{}]}", new TypeReference<V0<A>>() {
        });
        Assert.assertTrue(v1.getValue().get(0) instanceof A);
        
        V0<B> v2 = JSON.parseObject("{\"value\":[{}]}", new TypeReference<V0<B>>() {
        });
        Assert.assertTrue(v2.getValue().get(0) instanceof B);
    }

    private static class V<T> {

    }

    private static class V0<T> extends V<T> {

        private ArrayList<T> value;

        public ArrayList<T> getValue() {
            return value;
        }

        public void setValue(ArrayList<T> value) {
            this.value = value;
        }
    }

    public static class A {

    }

    public static class B {

    }
}
