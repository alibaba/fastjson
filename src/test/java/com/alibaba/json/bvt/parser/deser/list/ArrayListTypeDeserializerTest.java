package com.alibaba.json.bvt.parser.deser.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class ArrayListTypeDeserializerTest extends TestCase {

    public void test_null_0() throws Exception {
        Assert.assertNull(JSON.parseObject("null", new TypeReference<ArrayList<Integer>>() {
        }));
    }

    public void test_null_1() throws Exception {
        Assert.assertNull(JSON.parseObject("null", new TypeReference<Collection<Integer>>() {
        }));
    }

    public void test_null_2() throws Exception {
        Assert.assertNull(JSON.parseObject("{\"value\":null}", VO.class).getValue());
    }

    public void test_null_3() throws Exception {
        Assert.assertNull(JSON.parseObject("{\"value\":null}", V1.class).getValue());
    }

    public void test_empty() throws Exception {
        Assert.assertEquals(0, JSON.parseObject("[]", new TypeReference<ArrayList<Integer>>() {
        }).size());
        Assert.assertEquals(0, JSON.parseObject("[]", new TypeReference<Set<Integer>>() {
        }).size());

        Assert.assertEquals(0, JSON.parseObject("[]", new TypeReference<HashSet<Integer>>() {
        }).size());

        Assert.assertEquals(0, JSON.parseObject("{\"value\":[]}", VO.class).getValue().size());
    }

    public static class VO {

        private ArrayList<Integer> value;

        public ArrayList<Integer> getValue() {
            return value;
        }

        public void setValue(ArrayList<Integer> value) {
            this.value = value;
        }

    }

    private static class V1 {

        private ArrayList<Integer> value;

        public ArrayList<Integer> getValue() {
            return value;
        }

        @SuppressWarnings("unused")
        public void setValue(ArrayList<Integer> value) {
            this.value = value;
        }

    }
}
