package com.alibaba.json.bvt.parser.deser;

import java.util.Collection;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class CollectionFieldTest extends TestCase {

    public void test_null() throws Exception {
        Entity value = JSON.parseObject("{value:null}", Entity.class);
        Assert.assertNull(value.getValue());
    }

    public void test_empty() throws Exception {
        Entity value = JSON.parseObject("{value:[]}", Entity.class);
        Assert.assertEquals(0, value.getValue().size());
    }

    private static class Entity {

        private Collection value;

        public Collection getValue() {
            return value;
        }

        public void setValue(Collection value) {
            this.value = value;
        }
    }
}
