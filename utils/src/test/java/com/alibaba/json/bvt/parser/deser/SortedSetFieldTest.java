package com.alibaba.json.bvt.parser.deser;

import java.util.SortedSet;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class SortedSetFieldTest extends TestCase {

    public void test_null() throws Exception {
        Entity value = JSON.parseObject("{value:null}", Entity.class);
        Assert.assertNull(value.getValue());
    }

    public void test_empty() throws Exception {
        Entity value = JSON.parseObject("{value:[]}", Entity.class);
        Assert.assertEquals(0, value.getValue().size());
    }

    private static class Entity {

        private SortedSet value;

        public SortedSet getValue() {
            return value;
        }

        public void setValue(SortedSet value) {
            this.value = value;
        }
    }
}
