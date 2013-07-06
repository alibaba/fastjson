package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

public class IntegerFieldDeserializerTest3 extends TestCase {

    public void test_0() throws Exception {
        Entity a = JSON.parseObject("{f1:123, f2:null}", Entity.class);
        Assert.assertEquals(123, a.getF1());
        Assert.assertEquals(null, a.getF2());
    }

    public void test_1() throws Exception {
        Entity a = JSON.parseObject("{f1:22, f2:'33'}", Entity.class);
        Assert.assertEquals(22, a.getF1());
        Assert.assertEquals(33, a.getF2().intValue());
    }

    public void test_2() throws Exception {
        Entity a = JSON.parseObject("{f1:'22', f2:33}", Entity.class);
        Assert.assertEquals(22, a.getF1());
        Assert.assertEquals(33, a.getF2().intValue());
    }

    public static class Entity {

        private int     f1 = 124;
        private Integer f2 = 123;

        @JSONCreator
        public Entity(@JSONField(name="f1") int f1, @JSONField(name="f2") Integer f2){
            this.f1 = f1;
            this.f2 = f2;
        }

        public int getF1() {
            return f1;
        }

        public Integer getF2() {
            return f2;
        }

    }
}
