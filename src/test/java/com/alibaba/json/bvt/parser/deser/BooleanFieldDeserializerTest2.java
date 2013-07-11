package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

public class BooleanFieldDeserializerTest2 extends TestCase {

    public void test_0() throws Exception {
        Entity a = JSON.parseObject("{\"f1\":true,\"f2\":null}", Entity.class);
        Assert.assertEquals(true, a.getF1());
        Assert.assertEquals(null, a.getF2());
    }
    
    public void test_1() throws Exception {
        Entity a = JSON.parseObject("{\"f1\":1,\"f2\":null}", Entity.class);
        Assert.assertEquals(true, a.getF1());
        Assert.assertEquals(null, a.getF2());
    }
    
    public void test_2() throws Exception {
        Entity a = JSON.parseObject("{\"f1\":\"true\",\"f2\":null}", Entity.class);
        Assert.assertEquals(true, a.getF1());
        Assert.assertEquals(null, a.getF2());
    }

    public void test_3() throws Exception {
        Entity a = JSON.parseObject("{\"f1\":false,\"f2\":null}", Entity.class);
        Assert.assertEquals(false, a.getF1());
        Assert.assertEquals(null, a.getF2());
    }
    
    public static class Entity {

        private final Boolean f1;
        private final Boolean f2;

        @JSONCreator
        public Entity(@JSONField(name = "f1") Boolean f1, @JSONField(name = "f2") Boolean f2){
            this.f1 = f1;
            this.f2 = f2;
        }

        public Boolean getF1() {
            return f1;
        }

        public Boolean getF2() {
            return f2;
        }

    }
}
