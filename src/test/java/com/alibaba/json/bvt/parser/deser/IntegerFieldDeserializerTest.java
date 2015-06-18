package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class IntegerFieldDeserializerTest extends TestCase {

    public void test_0() throws Exception {
        Entity a = JSON.parseObject("{f1:null, f2:null}", Entity.class);
        Assert.assertEquals(124, a.getF1());
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

        public int getF1() {
            return f1;
        }

        public void setF1(int f1) {
            this.f1 = f1;
        }

        public Integer getF2() {
            return f2;
        }

        public void setF2(Integer f2) {
            this.f2 = f2;
        }

    }
}
