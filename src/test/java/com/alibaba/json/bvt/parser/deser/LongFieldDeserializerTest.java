package com.alibaba.json.bvt.parser.deser;

import java.util.UUID;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class LongFieldDeserializerTest extends TestCase {

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
        Assert.assertEquals(33, a.getF2().longValue());
    }

    public void test_error() throws Exception {
        JSONException ex = null;
        try {
            JSON.parseObject("{f3:44}", UUID.class);
        } catch (JSONException e) {
            ex = e;
        }
        Assert.assertNotNull(ex);
    }

    public static class Entity {

        private long f1 = 124;
        private Long f2 = 123L;

        public long getF1() {
            return f1;
        }

        public void setF1(long f1) {
            this.f1 = f1;
        }

        public Long getF2() {
            return f2;
        }

        public void setF2(Long f2) {
            this.f2 = f2;
        }

        public void setF3(Long v) {
            throw new RuntimeException();
        }
    }
}
