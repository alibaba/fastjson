package com.alibaba.json.bvt.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Case0 extends TestCase {

    public void test_0() throws Exception {
        V0 entity = new V0();
        entity.setValue("abc");

        String jsonString = JSON.toJSONString(entity);

        System.out.println(jsonString);

        V0 entity2 = JSON.parseObject(jsonString, V0.class);
        Assert.assertEquals(entity.getValue(), entity2.getValue());
    }

    public static class V0 {

        private int    id;
        private String value;

        private long   v2;

        public long getV2() {
            return v2;
        }

        public void setV2(long v2) {
            this.v2 = v2;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
