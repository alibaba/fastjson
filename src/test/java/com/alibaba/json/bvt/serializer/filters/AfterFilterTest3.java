package com.alibaba.json.bvt.serializer.filters;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.AfterFilter;

public class AfterFilterTest3 extends TestCase {

    public void test_afterFilter() throws Exception {
        AfterFilter filter = new AfterFilter() {

            @Override
            public void writeAfter(Object object) {
                this.writeKeyValue("id", 123);
            }
        };
        Assert.assertEquals(JSON.toJSONString(new VO(), filter), "{\"value\":1001,\"id\":123}");
    }

    public void test_afterFilter2() throws Exception {
        AfterFilter filter = new AfterFilter() {

            @Override
            public void writeAfter(Object object) {
                this.writeKeyValue("id", 123);
                this.writeKeyValue("name", "wenshao");
            }
        };
        Assert.assertEquals(JSON.toJSONString(new VO(), filter), "{\"value\":1001,\"id\":123,\"name\":\"wenshao\"}");
    }

    public static class VO {

        private int value = 1001;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
