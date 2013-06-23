package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class ParseRestTest extends TestCase {

    public void test_parseRest_0() throws Exception {
        String text = "{\"f3\":333,\"f2\":222}";
        Entity entity = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(0, entity.getF0());
        Assert.assertEquals(0, entity.getF1());
        Assert.assertEquals(222, entity.getF2());
        Assert.assertEquals(333, entity.getF3());
    }

    public static class Entity {

        private int f0;
        private int f1;
        private int f2;
        private int f3;

        public int getF0() {
            return f0;
        }

        public void setF0(int f0) {
            this.f0 = f0;
        }

        public int getF1() {
            return f1;
        }

        public void setF1(int f1) {
            this.f1 = f1;
        }

        public int getF2() {
            return f2;
        }

        public void setF2(int f2) {
            this.f2 = f2;
        }

        public int getF3() {
            return f3;
        }

        public void setF3(int f3) {
            this.f3 = f3;
        }

    }
}
